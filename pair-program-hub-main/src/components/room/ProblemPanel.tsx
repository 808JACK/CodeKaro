import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { ChevronLeft, ChevronRight, Crown } from 'lucide-react';

interface TestCase {
  input: string;
  output: string;
  explanation?: string;
}

interface Problem {
  id: string;
  title: string;
  difficulty: 'Easy' | 'Medium' | 'Hard';
  description: string;
  examples: TestCase[];
  constraints?: string[];
  topic: string;
}

interface ProblemPanelProps {
  problems: Problem[];
  currentProblemIndex: number;
  onProblemChange: (index: number) => void;
  isAdmin: boolean;
}

export const ProblemPanel = ({
  problems,
  currentProblemIndex,
  onProblemChange,
  isAdmin
}: ProblemPanelProps) => {
  const currentProblem = problems[currentProblemIndex];

  if (!currentProblem) {
    return (
      <div className="flex items-center justify-center h-full text-muted-foreground">
        No problems available
      </div>
    );
  }

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'Easy': return 'text-success';
      case 'Medium': return 'text-warning';
      case 'Hard': return 'text-destructive';
      default: return 'text-muted-foreground';
    }
  };

  return (
    <div className="h-full flex flex-col">
      {/* Problem Header */}
      <div className="p-4 border-b border-border">
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-2">
            <h2 className="text-xl font-bold">{currentProblem.title}</h2>
            <Badge 
              variant="outline" 
              className={getDifficultyColor(currentProblem.difficulty)}
            >
              {currentProblem.difficulty}
            </Badge>
            <Badge variant="secondary">
              {currentProblem.topic}
            </Badge>
          </div>
          
          {isAdmin && (
            <div className="flex items-center gap-1">
              <Crown className="h-4 w-4 text-warning" />
              <span className="text-xs text-muted-foreground">Admin</span>
            </div>
          )}
        </div>

        {/* Problem Navigation - Only visible to admin */}
        {isAdmin && problems.length > 1 && (
          <div className="flex items-center justify-between">
            <Button
              variant="outline"
              size="sm"
              onClick={() => onProblemChange(Math.max(0, currentProblemIndex - 1))}
              disabled={currentProblemIndex === 0}
            >
              <ChevronLeft className="h-4 w-4 mr-1" />
              Previous
            </Button>
            
            <span className="text-sm text-muted-foreground">
              {currentProblemIndex + 1} of {problems.length}
            </span>
            
            <Button
              variant="outline"
              size="sm"
              onClick={() => onProblemChange(Math.min(problems.length - 1, currentProblemIndex + 1))}
              disabled={currentProblemIndex === problems.length - 1}
            >
              Next
              <ChevronRight className="h-4 w-4 ml-1" />
            </Button>
          </div>
        )}
      </div>

      {/* Problem Content */}
      <div className="flex-1 p-4 overflow-y-auto space-y-6">
        {/* Description */}
        <div>
          <h3 className="font-semibold mb-2">Description</h3>
          <p className="text-sm leading-relaxed">{currentProblem.description}</p>
        </div>

        {/* Examples */}
        <div>
          <h3 className="font-semibold mb-3">Examples</h3>
          <div className="space-y-4">
            {currentProblem.examples?.map((example, index) => (
              <Card key={index} className="bg-secondary/20">
                <CardContent className="p-4">
                  <div className="space-y-2">
                    <div>
                      <span className="text-xs font-medium text-muted-foreground">Input:</span>
                      <code className="block mt-1 p-2 bg-background rounded text-sm font-mono">
                        {example.input}
                      </code>
                    </div>
                    <div>
                      <span className="text-xs font-medium text-muted-foreground">Output:</span>
                      <code className="block mt-1 p-2 bg-background rounded text-sm font-mono">
                        {example.output}
                      </code>
                    </div>
                    {example.explanation && (
                      <div>
                        <span className="text-xs font-medium text-muted-foreground">Explanation:</span>
                        <p className="text-sm mt-1">{example.explanation}</p>
                      </div>
                    )}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>

        {/* Constraints */}
        {currentProblem.constraints && (
          <div>
            <h3 className="font-semibold mb-2">Constraints</h3>
            <ul className="text-sm space-y-1">
              {currentProblem.constraints?.map((constraint, index) => (
                <li key={index} className="flex items-start gap-2">
                  <span className="text-muted-foreground">•</span>
                  <span>{constraint}</span>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
};