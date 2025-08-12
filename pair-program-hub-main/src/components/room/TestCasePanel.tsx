import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Textarea } from '@/components/ui/textarea';
import { Badge } from '@/components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { 
  Play, 
  Plus, 
  Edit3, 
  Save, 
  X, 
  CheckCircle, 
  XCircle,
  Clock
} from 'lucide-react';

interface TestCase {
  id: string;
  inputs: { name: string; value: string }[]; // Separate inputs for each parameter
  expectedOutput: string;
  actualOutput?: string;
  status?: 'passed' | 'failed' | 'running';
  runtime?: number;
}

interface TestCasePanelProps {
  testCases: TestCase[];
  onRunTests: (testCases: TestCase[]) => void;
  onAddTestCase?: (testCase: Omit<TestCase, 'id'>) => void;
  onEditTestCase?: (id: string, testCase: Partial<TestCase>) => void;
  onDeleteTestCase?: (id: string) => void;
  isRunning?: boolean;
  currentProblem?: any;
}

export const TestCasePanel = ({
  testCases,
  onRunTests,
  onAddTestCase,
  onEditTestCase,
  onDeleteTestCase,
  isRunning = false,
  currentProblem
}: TestCasePanelProps) => {
  const [editingCase, setEditingCase] = useState<string | null>(null);
  const [newCase, setNewCase] = useState({ inputs: [{ name: '', value: '' }], expectedOutput: '' });
  const [isAddingNew, setIsAddingNew] = useState(false);

  const handleSaveEdit = (id: string, inputs: { name: string; value: string }[], expectedOutput: string) => {
    if (onEditTestCase) {
      onEditTestCase(id, { inputs, expectedOutput });
    }
    setEditingCase(null);
  };

  const handleAddNew = () => {
    const hasValidInputs = newCase.inputs.every(input => input.name.trim() && input.value.trim());
    if (hasValidInputs && newCase.expectedOutput.trim() && onAddTestCase) {
      onAddTestCase(newCase);
      setNewCase({ inputs: [{ name: '', value: '' }], expectedOutput: '' });
      setIsAddingNew(false);
    }
  };

  const getStatusIcon = (status?: string) => {
    switch (status) {
      case 'passed':
        return <CheckCircle className="h-4 w-4 text-success" />;
      case 'failed':
        return <XCircle className="h-4 w-4 text-destructive" />;
      case 'running':
        return <Clock className="h-4 w-4 text-warning animate-spin" />;
      default:
        return null;
    }
  };

  const passedTests = testCases.filter(tc => tc.status === 'passed').length;
  const totalTests = testCases.length;

  return (
    <div className="h-full flex flex-col">
      {/* Header */}
      <div className="p-4 border-b border-border">
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-3">
            <h3 className="font-semibold">Test Cases</h3>
            {totalTests > 0 && (
              <Badge variant={passedTests === totalTests ? 'default' : 'secondary'}>
                {passedTests}/{totalTests} Passed
              </Badge>
            )}
          </div>
          
          <div className="flex items-center gap-2">
            {onAddTestCase && (
              <Button
                variant="outline"
                size="sm"
                onClick={() => setIsAddingNew(true)}
                disabled={isRunning}
              >
                <Plus className="h-4 w-4 mr-1" />
                Add
              </Button>
            )}
            <Button
              size="sm"
              onClick={() => onRunTests(testCases)}
              disabled={isRunning || testCases.length === 0}
            >
              <Play className="h-4 w-4 mr-1" />
              {isRunning ? 'Running...' : 'Run All'}
            </Button>
          </div>
        </div>
      </div>

      {/* Test Cases List */}
      <div className="flex-1 overflow-y-auto">
        <Tabs defaultValue="cases" className="h-full">
          <TabsList className="grid w-full grid-cols-2 m-2">
            <TabsTrigger value="cases">Test Cases</TabsTrigger>
            <TabsTrigger value="results">Results</TabsTrigger>
          </TabsList>
          
          <TabsContent value="cases" className="p-4 space-y-3 h-full overflow-y-auto">
            {/* Add New Test Case */}
            {isAddingNew && (
              <Card className="border-dashed border-primary/50">
                <CardHeader className="pb-3">
                  <CardTitle className="text-sm flex items-center justify-between">
                    New Test Case
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => setIsAddingNew(false)}
                    >
                      <X className="h-4 w-4" />
                    </Button>
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <label className="text-xs font-medium text-muted-foreground">Inputs</label>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setNewCase(prev => ({
                          ...prev,
                          inputs: [...prev.inputs, { name: '', value: '' }]
                        }))}
                      >
                        <Plus className="h-3 w-3" />
                      </Button>
                    </div>
                    {newCase.inputs.map((input, idx) => (
                      <div key={idx} className="flex gap-2 items-end">
                        <div className="flex-1">
                          <input
                            type="text"
                            value={input.name}
                            onChange={(e) => setNewCase(prev => ({
                              ...prev,
                              inputs: prev.inputs.map((inp, i) => 
                                i === idx ? { ...inp, name: e.target.value } : inp
                              )
                            }))}
                            className="w-full px-2 py-1 text-xs border border-border rounded"
                            placeholder="Parameter name"
                          />
                        </div>
                        <div className="flex-2">
                          <Textarea
                            value={input.value}
                            onChange={(e) => setNewCase(prev => ({
                              ...prev,
                              inputs: prev.inputs.map((inp, i) => 
                                i === idx ? { ...inp, value: e.target.value } : inp
                              )
                            }))}
                            className="font-mono text-sm"
                            rows={2}
                            placeholder="Value"
                          />
                        </div>
                        {newCase.inputs.length > 1 && (
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setNewCase(prev => ({
                              ...prev,
                              inputs: prev.inputs.filter((_, i) => i !== idx)
                            }))}
                          >
                            <X className="h-3 w-3" />
                          </Button>
                        )}
                      </div>
                    ))}
                  </div>
                  <div>
                    <label className="text-xs font-medium text-muted-foreground">Expected Output</label>
                    <Textarea
                      value={newCase.expectedOutput}
                      onChange={(e) => setNewCase(prev => ({ ...prev, expectedOutput: e.target.value }))}
                      className="mt-1 font-mono text-sm"
                      rows={2}
                      placeholder="Enter expected output..."
                    />
                  </div>
                  <div className="flex gap-2">
                    <Button size="sm" onClick={handleAddNew}>
                      <Save className="h-4 w-4 mr-1" />
                      Save
                    </Button>
                    <Button variant="outline" size="sm" onClick={() => setIsAddingNew(false)}>
                      Cancel
                    </Button>
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Existing Test Cases */}
            {testCases.map((testCase, index) => (
              <TestCaseCard
                key={testCase.id}
                testCase={testCase}
                index={index}
                isEditing={editingCase === testCase.id}
                onEdit={() => setEditingCase(testCase.id)}
                onSave={handleSaveEdit}
                onCancel={() => setEditingCase(null)}
                onDelete={() => onDeleteTestCase?.(testCase.id)}
                getStatusIcon={getStatusIcon}
              />
            ))}

            {testCases.length === 0 && !isAddingNew && (
              <div className="text-center py-8 text-muted-foreground">
                <p>No test cases yet</p>
                <Button
                  variant="outline"
                  size="sm"
                  className="mt-2"
                  onClick={() => setIsAddingNew(true)}
                >
                  <Plus className="h-4 w-4 mr-1" />
                  Add your first test case
                </Button>
              </div>
            )}
          </TabsContent>

          <TabsContent value="results" className="p-4 space-y-3 h-full overflow-y-auto">
            {testCases.map((testCase, index) => (
              <Card key={testCase.id} className={`
                ${testCase.status === 'passed' ? 'border-success/50' : ''}
                ${testCase.status === 'failed' ? 'border-destructive/50' : ''}
              `}>
                <CardHeader className="pb-3">
                  <CardTitle className="text-sm flex items-center justify-between">
                    <span>Test Case {index + 1}</span>
                    <div className="flex items-center gap-2">
                      {getStatusIcon(testCase.status)}
                      {testCase.runtime && (
                        <span className="text-xs text-muted-foreground">
                          {testCase.runtime}ms
                        </span>
                      )}
                    </div>
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-3">
                   <div>
                     <label className="text-xs font-medium text-muted-foreground">Inputs</label>
                     {testCase.inputs.map((input, idx) => (
                       <div key={idx} className="mt-1">
                         <code className="block p-2 bg-secondary/30 rounded text-sm font-mono">
                           <span className="text-muted-foreground">{input.name}:</span> {input.value}
                         </code>
                       </div>
                     ))}
                   </div>
                  
                  <div>
                    <label className="text-xs font-medium text-muted-foreground">Expected</label>
                    <code className="block mt-1 p-2 bg-secondary/30 rounded text-sm font-mono">
                      {testCase.expectedOutput}
                    </code>
                  </div>

                  {testCase.actualOutput && (
                    <div>
                      <label className="text-xs font-medium text-muted-foreground">Actual</label>
                      <code className={`
                        block mt-1 p-2 rounded text-sm font-mono
                        ${testCase.status === 'passed' ? 'bg-success/10' : 'bg-destructive/10'}
                      `}>
                        {testCase.actualOutput}
                      </code>
                    </div>
                  )}
                </CardContent>
              </Card>
            ))}
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

// Separate component for individual test case to avoid complexity
const TestCaseCard = ({
  testCase,
  index,
  isEditing,
  onEdit,
  onSave,
  onCancel,
  onDelete,
  getStatusIcon
}: {
  testCase: TestCase;
  index: number;
  isEditing: boolean;
  onEdit: () => void;
  onSave: (id: string, inputs: { name: string; value: string }[], expectedOutput: string) => void;
  onCancel: () => void;
  onDelete: () => void;
  getStatusIcon: (status?: string) => React.ReactNode;
}) => {
  const [editInputs, setEditInputs] = useState(testCase.inputs);
  const [editOutput, setEditOutput] = useState(testCase.expectedOutput);

  if (isEditing) {
    return (
      <Card className="border-primary/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-sm flex items-center justify-between">
            Edit Test Case {index + 1}
            <Button variant="ghost" size="sm" onClick={onCancel}>
              <X className="h-4 w-4" />
            </Button>
          </CardTitle>
        </CardHeader>
         <CardContent className="space-y-3">
           <div>
             <div className="flex items-center justify-between mb-2">
               <label className="text-xs font-medium text-muted-foreground">Inputs</label>
               <Button
                 variant="ghost"
                 size="sm"
                 onClick={() => setEditInputs([...editInputs, { name: '', value: '' }])}
               >
                 <Plus className="h-3 w-3" />
               </Button>
             </div>
             {editInputs.map((input, idx) => (
               <div key={idx} className="flex gap-2 items-end">
                 <div className="flex-1">
                   <input
                     type="text"
                     value={input.name}
                     onChange={(e) => setEditInputs(prev => prev.map((inp, i) => 
                       i === idx ? { ...inp, name: e.target.value } : inp
                     ))}
                     className="w-full px-2 py-1 text-xs border border-border rounded"
                     placeholder="Parameter name"
                   />
                 </div>
                 <div className="flex-2">
                   <Textarea
                     value={input.value}
                     onChange={(e) => setEditInputs(prev => prev.map((inp, i) => 
                       i === idx ? { ...inp, value: e.target.value } : inp
                     ))}
                     className="font-mono text-sm"
                     rows={2}
                     placeholder="Value"
                   />
                 </div>
                 {editInputs.length > 1 && (
                   <Button
                     variant="ghost"
                     size="sm"
                     onClick={() => setEditInputs(prev => prev.filter((_, i) => i !== idx))}
                   >
                     <X className="h-3 w-3" />
                   </Button>
                 )}
               </div>
             ))}
           </div>
          <div>
            <label className="text-xs font-medium text-muted-foreground">Expected Output</label>
            <Textarea
              value={editOutput}
              onChange={(e) => setEditOutput(e.target.value)}
              className="mt-1 font-mono text-sm"
              rows={2}
            />
          </div>
           <div className="flex gap-2">
             <Button size="sm" onClick={() => onSave(testCase.id, editInputs, editOutput)}>
               <Save className="h-4 w-4 mr-1" />
               Save
             </Button>
            <Button variant="outline" size="sm" onClick={onCancel}>
              Cancel
            </Button>
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader className="pb-3">
        <CardTitle className="text-sm flex items-center justify-between">
          <span>Test Case {index + 1}</span>
          <div className="flex items-center gap-1">
            {getStatusIcon(testCase.status)}
            <Button variant="ghost" size="sm" onClick={onEdit}>
              <Edit3 className="h-4 w-4" />
            </Button>
            <Button variant="ghost" size="sm" onClick={onDelete}>
              <X className="h-4 w-4" />
            </Button>
          </div>
        </CardTitle>
      </CardHeader>
       <CardContent className="space-y-3">
         <div>
           <label className="text-xs font-medium text-muted-foreground">Inputs</label>
           {testCase.inputs.map((input, idx) => (
             <div key={idx} className="mt-1">
               <code className="block p-2 bg-secondary/30 rounded text-sm font-mono">
                 <span className="text-muted-foreground">{input.name}:</span> {input.value}
               </code>
             </div>
           ))}
         </div>
        <div>
          <label className="text-xs font-medium text-muted-foreground">Expected Output</label>
          <code className="block mt-1 p-2 bg-secondary/30 rounded text-sm font-mono">
            {testCase.expectedOutput}
          </code>
        </div>
      </CardContent>
    </Card>
  );
};
