import { useEffect, useRef, useState } from 'react';
import Editor from '@monaco-editor/react';
import { editor } from 'monaco-editor';

interface ContestMonacoEditorProps {
  value: string;
  onChange: (value: string) => void;
  language?: string;
  theme?: string;
  height?: string;
  readOnly?: boolean;
  showLineNumbers?: boolean;
  showMinimap?: boolean;
  fontSize?: number;
  wordWrap?: string;
  automaticLayout?: boolean;
}

export const ContestMonacoEditor = ({
  value,
  onChange,
  language = 'python',
  theme = 'vs-dark',
  height = '100%',
  readOnly = false,
  showLineNumbers = true,
  showMinimap = true,
  fontSize = 14,
  wordWrap = 'on',
  automaticLayout = true
}: ContestMonacoEditorProps) => {
  const editorRef = useRef<editor.IStandaloneCodeEditor | null>(null);

  const handleEditorDidMount = (editorInstance: editor.IStandaloneCodeEditor) => {
    editorRef.current = editorInstance;
  };

  const handleEditorChange = (newValue: string | undefined) => {
    if (newValue !== undefined) {
      onChange(newValue);
    }
  };

  useEffect(() => {
    // Update editor value if it changes externally
    if (editorRef.current) {
      const model = editorRef.current.getModel();
      if (model && model.getValue() !== value) {
        model.setValue(value);
      }
    }
  }, [value]);

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
          minimap: { enabled: showMinimap },
          fontSize: fontSize,
          lineNumbers: showLineNumbers ? 'on' : 'off',
          roundedSelection: false,
          scrollBeyondLastLine: false,
          automaticLayout: automaticLayout,
          wordWrap: wordWrap as any,
          tabSize: 2,
          insertSpaces: true,
          formatOnPaste: true,
          formatOnType: true,
          selectOnLineNumbers: true,
          cursorBlinking: 'smooth',
          cursorSmoothCaretAnimation: 'on',
          smoothScrolling: true,
          mouseWheelZoom: true,
          readOnly: readOnly
        }}
      />
    </div>
  );
};
