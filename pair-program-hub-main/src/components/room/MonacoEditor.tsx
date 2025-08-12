import { useEffect, useRef, useState } from 'react';
import Editor from '@monaco-editor/react';
import { editor } from 'monaco-editor';
import { MonacoAdapter, Client, TextOperation, OTSelection, IClientCallbacks } from '@/lib/ot-client';

interface MonacoEditorProps {
  value: string;
  onChange: (value: string) => void;
  language?: string;
  theme?: string;
  height?: string;
  onCursorChange?: (position: { line: number; column: number }) => void;
  roomId?: string;
  userId?: string;
}

export const MonacoEditor = ({
  value,
  onChange,
  language = 'python',
  theme = 'vs-dark',
  height = '100%',
  onCursorChange,
  roomId,
  userId = 'user-1'
}: MonacoEditorProps) => {
  const editorRef = useRef<editor.IStandaloneCodeEditor | null>(null);
  const adapterRef = useRef<MonacoAdapter | null>(null);
  const clientRef = useRef<Client | null>(null);
  const [isReady, setIsReady] = useState(false);

  // Mock WebSocket operations for now
  const sendOperation = (revision: number, operation: TextOperation) => {
    console.log('Sending operation:', { revision, operation: operation.toJSON() });
    // Simulate server acknowledgment after a delay
    setTimeout(() => {
      clientRef.current?.serverAck();
    }, 100);
  };

  const sendSelection = (selection: OTSelection | null) => {
    console.log('Sending selection:', selection?.toJSON());
    // In real implementation, this would broadcast to other users
  };

  const applyOperation = (operation: TextOperation) => {
    console.log('Applying operation:', operation.toJSON());
    if (adapterRef.current) {
      adapterRef.current.applyOperation(operation);
    }
  };

  const handleEditorDidMount = (editorInstance: editor.IStandaloneCodeEditor) => {
    editorRef.current = editorInstance;
    
    // Initialize Monaco adapter for OT
    const adapter = new MonacoAdapter(editorInstance);
    adapterRef.current = adapter;

    // Initialize OT client
    const callbacks: IClientCallbacks = {
      sendOperation,
      applyOperation,
      sendSelection,
      getSelection: () => adapter.getSelection(),
      setSelection: (selection) => adapter.setSelection(selection)
    };

    const client = new Client(0, userId, callbacks);
    clientRef.current = client;

    // Register adapter callbacks
    adapter.registerCallbacks({
      change: (operation, inverse) => {
        client.applyClient(operation);
        // Update parent component with new value
        const newValue = editorInstance.getModel()?.getValue() || '';
        onChange(newValue);
      },
      selectionChange: () => {
        client.selectionChanged();
        // Notify parent of cursor position change
        const position = editorInstance.getPosition();
        if (position && onCursorChange) {
          onCursorChange({ line: position.lineNumber, column: position.column });
        }
      },
      blur: () => {
        client.blur();
      }
    });

    setIsReady(true);

    // Note: Real-time collaboration would be implemented here
    // For now, just initialize the editor without simulated operations
  };

  const handleEditorChange = (newValue: string | undefined) => {
    // This is already handled by the OT system
    // Just update if OT is not initialized yet
    if (!isReady && newValue !== undefined) {
      onChange(newValue);
    }
  };

  useEffect(() => {
    return () => {
      // Cleanup
      if (adapterRef.current) {
        adapterRef.current.detach();
      }
    };
  }, []);

  useEffect(() => {
    // Update editor value if it changes externally and OT is not handling it
    if (editorRef.current && !isReady) {
      const model = editorRef.current.getModel();
      if (model && model.getValue() !== value) {
        model.setValue(value);
      }
    }
  }, [value, isReady]);

  return (
    <div className="h-full border border-border rounded-lg overflow-hidden">
      <Editor
        height={height}
        language={language}
        theme={theme}
        value={value}
        onChange={handleEditorChange}
        onMount={handleEditorDidMount}
        options={{
          minimap: { enabled: false },
          fontSize: 14,
          lineNumbers: 'on',
          roundedSelection: false,
          scrollBeyondLastLine: false,
          automaticLayout: true,
          wordWrap: 'on',
          tabSize: 2,
          insertSpaces: true,
          formatOnPaste: true,
          formatOnType: true,
          selectOnLineNumbers: true,
          cursorBlinking: 'smooth',
          cursorSmoothCaretAnimation: 'on',
          smoothScrolling: true,
          mouseWheelZoom: true
        }}
      />
    </div>
  );
};