import { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Input } from '@/components/ui/input';
import { 
  Code, 
  Users, 
  MessageCircle, 
  Play, 
  Trophy, 
  Clock, 
  Copy,
  Menu,
  Crown,
  ChevronDown,
  X,
  CheckCircle,
  XCircle,
  Loader2,
  Send
} from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { MonacoEditor } from '@/components/room/MonacoEditor';
import { RoomSidebar } from '@/components/room/RoomSidebar';
import { Badge } from '@/components/ui/badge';
import { Card, CardContent } from '@/components/ui/card';
import { TestCasePanel } from '@/components/room/TestCasePanel';

// Mock API functions - simplified for general rooms
const mockAPI = {
  async getProblemTemplate(problemId: string, language: string) {
    await new Promise(resolve => setTimeout(resolve, 500)); // Simulate API delay
    
    // For general rooms, return a generic template since they don't have specific problems
    // Contest rooms should use ContestRoom.tsx which loads real templates from backend
    return `// Write your solution here`;
  },

  async getProblemsFromSections() {
    await new Promise(resolve => setTimeout(resolve, 800));
    
    // Return empty problems for general rooms - they should not have hardcoded problems
    // Contest rooms should use ContestRoom.tsx which loads real problems from backend
    return {
      'arrays': [],
      'linked-list': [],
      'trees': [],
      'graphs': [],
      'dynamic-programming': []
    };
  }
};

// Types
interface TestCase {
  id: string;
  inputs: { name: string; value: string }[]; // Separate inputs for each parameter
  expectedOutput: string;
  actualOutput?: string;
  status?: 'passed' | 'failed' | 'running';
  runtime?: number;
}

interface Problem {
  id: string;
  title: string;
  difficulty: 'Easy' | 'Medium' | 'Hard';
  description: string;
  examples: {
    input: string;
    output: string;
    explanation?: string;
  }[];
  constraints?: string[];
  topic: string;
}

interface Participant {
  id: string;
  name: string;
  avatar: string;
  isOnline: boolean;
  cursor?: { line: number; col: number } | null;
}

interface ChatMessage {
  id: string;
  user: string;
  message: string;
  timestamp: string;
}

const Room = () => {
  const { roomCode } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { toast } = useToast();
  
  const [code, setCode] = useState('// Loading template...');
  const [currentProblem, setCurrentProblem] = useState(0);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState<'participants' | 'chat' | 'leaderboard'>('participants');
  const [isJoining, setIsJoining] = useState(false);
  const [isSendingMessage, setIsSendingMessage] = useState(false);
  const [language, setLanguage] = useState('python');
  const [problems, setProblems] = useState<Problem[]>([]);
  const [loadingTemplate, setLoadingTemplate] = useState(false);
  const [availableSections] = useState(['arrays', 'linked-list', 'trees', 'graphs', 'dynamic-programming']);
  const [selectedSections, setSelectedSections] = useState<string[]>(['arrays']);
  const [loadingProblems, setLoadingProblems] = useState(false);
  const [showSidebar, setShowSidebar] = useState(false);
  
  const { roomName, isContest } = location.state || {};
  const isAdmin = true; // Mock - first user is admin

  const participants: Participant[] = [
    { id: '1', name: 'You', avatar: '👤', isOnline: true, cursor: { line: 3, col: 15 } },
    { id: '2', name: 'Alex', avatar: '🧑‍💻', isOnline: true, cursor: { line: 8, col: 20 } },
    { id: '3', name: 'Sam', avatar: '👩‍💻', isOnline: false, cursor: null }
  ];

  const [chatMessages, setChatMessages] = useState<ChatMessage[]>([
    { id: '1', user: 'Alex', message: 'Should we use a hash map approach?', timestamp: '10:23 AM' },
    { id: '2', user: 'You', message: 'Yes, that would be O(n) time complexity', timestamp: '10:24 AM' },
    { id: '3', user: 'Sam', message: 'Let me implement it', timestamp: '10:25 AM' }
  ]);

  const leaderboard = isContest ? [
    { rank: 1, name: 'Alex', score: 150, problems: 2, time: '45:23' },
    { rank: 2, name: 'You', score: 100, problems: 1, time: '52:10' },
    { rank: 3, name: 'Sam', score: 75, problems: 1, time: '58:45' }
  ] : [];

  // Load problems from selected sections
  const loadProblemsFromSections = async () => {
    setLoadingProblems(true);
    try {
      const allProblems = await mockAPI.getProblemsFromSections();
      const selectedProblems = selectedSections.flatMap(section => allProblems[section] || []);
      setProblems(selectedProblems);
      setCurrentProblem(0);
      
      // Load template for first problem
      if (selectedProblems.length > 0) {
        loadTemplate(selectedProblems[0].id, language);
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load problems",
        variant: "destructive"
      });
    } finally {
      setLoadingProblems(false);
    }
  };

  // Load coding template
  const loadTemplate = async (problemId: string, lang: string) => {
    setLoadingTemplate(true);
    try {
      const template = await mockAPI.getProblemTemplate(problemId, lang);
      setCode(template);
    } catch (error) {
      toast({
        title: "Error", 
        description: "Failed to load coding template",
        variant: "destructive"
      });
    } finally {
      setLoadingTemplate(false);
    }
  };

  // Test cases state - loaded from API with proper input structure
  const [testCases, setTestCases] = useState<TestCase[]>([]);
  
  // Load test cases from backend API instead of hardcoded mock data
  const loadTestCases = async (problemId: string) => {
    // For general rooms, return empty test cases since they don't have specific problems
    // Contest rooms should use ContestRoom.tsx which loads real test cases from backend
    await new Promise(resolve => setTimeout(resolve, 300));
    return [];
  };

  // Global contest timer - same end time for everyone
  const [contestDuration] = useState(isContest ? 3600 : null); // 1 hour global duration
  const [contestStartTime] = useState(isContest ? Date.now() : null);
  const [timeLeft, setTimeLeft] = useState(contestDuration);

  useEffect(() => {
    // Load initial problems
    loadProblemsFromSections();
    
    // Simulate joining room
    setIsJoining(true);
    setTimeout(() => {
      setIsJoining(false);
      toast({
        title: "Joined Room Successfully!",
        description: `Welcome to ${roomName || 'Coding Session'}`
      });
    }, 2000);
  }, [roomName, toast]);

  // Load test cases when problem changes
  useEffect(() => {
    const currentProblemData = problems[currentProblem];
    if (currentProblemData) {
      loadTestCases(currentProblemData.id).then(setTestCases);
    }
  }, [currentProblem, problems]);

  useEffect(() => {
    // Reload template when language changes
    const currentProblemData = problems[currentProblem];
    if (currentProblemData) {
      loadTemplate(currentProblemData.id, language);
    }
  }, [language, currentProblem, problems]);

  // Global contest timer - ends for everyone at same time
  useEffect(() => {
    if (isContest && contestStartTime && contestDuration) {
      const timer = setInterval(() => {
        const elapsed = Math.floor((Date.now() - contestStartTime) / 1000);
        const remaining = Math.max(0, contestDuration - elapsed);
        setTimeLeft(remaining);
        
        if (remaining === 0) {
          toast({
            title: "Contest Ended!",
            description: "Time is up! Contest has concluded for all participants.",
            variant: "destructive"
          });
        }
      }, 1000);
      return () => clearInterval(timer);
    }
  }, [isContest, contestStartTime, contestDuration, toast]);

  const formatTime = (seconds: number) => {
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${hrs.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const handleRunTests = (casesToRun?: TestCase[]) => {
    const targetCases = casesToRun || testCases;
    
    toast({
      title: "Running Tests...",
      description: `Executing ${targetCases.length} test cases`
    });
    
    // Update test cases in the SAME test case section
    const updatedTests = targetCases.map(tc => ({
      ...tc,
      status: 'running' as const,
      actualOutput: undefined,
      runtime: undefined
    }));
    
    // Update only the specific test cases that are running
    setTestCases(prev => 
      prev.map(existingCase => {
        const runningCase = updatedTests.find(tc => tc.id === existingCase.id);
        return runningCase || existingCase;
      })
    );

    setTimeout(() => {
      // Show results in the same test case panel
      setTestCases(prev => 
        prev.map(existingCase => {
          const runningCase = targetCases.find(tc => tc.id === existingCase.id);
          if (runningCase) {
            return {
              ...runningCase,
              status: Math.random() > 0.3 ? 'passed' as const : 'failed' as const,
              actualOutput: existingCase.id === '1' ? '[0,1]' : Math.random() > 0.5 ? '[1,2]' : 'null',
              runtime: Math.floor(Math.random() * 100) + 10
            };
          }
          return existingCase;
        })
      );
      
      const passed = targetCases.filter(() => Math.random() > 0.3).length;
      toast({
        title: "Test Results",
        description: `${passed}/${targetCases.length} test cases passed`
      });
    }, 2000);
  };

  const handleSubmit = () => {
    toast({
      title: "Submitting Solution...",
      description: "Your code is being evaluated"
    });
    
    setTimeout(() => {
      toast({
        title: "Accepted!",
        description: "Your solution passed all test cases",
      });
    }, 3000);
  };

  const handleSendMessage = (message: string) => {
    setIsSendingMessage(true);
    setTimeout(() => {
      const newMessage: ChatMessage = {
        id: Date.now().toString(),
        user: 'You',
        message: message,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      };
      setChatMessages(prev => [...prev, newMessage]);
      setIsSendingMessage(false);
      toast({
        title: "Message sent",
        description: message
      });
    }, 2000);
  };

  const copyRoomCode = () => {
    navigator.clipboard.writeText(roomCode || '');
    toast({
      title: "Room code copied!",
      description: "Share this code with others to join"
    });
  };

  const handleProblemChange = (index: number) => {
    if (isAdmin && problems[index]) {
      setCurrentProblem(index);
      loadTemplate(problems[index].id, language);
      // Broadcast to all users in room
      toast({
        title: "Problem Changed",
        description: `Admin switched to: ${problems[index].title}`
      });
    }
  };

  const handleNextProblem = () => {
    if (currentProblem < problems.length - 1) {
      handleProblemChange(currentProblem + 1);
    }
  };

  const handlePrevProblem = () => {
    if (currentProblem > 0) {
      handleProblemChange(currentProblem - 1);
    }
  };

  const handleSectionChange = (sections: string[]) => {
    setSelectedSections(sections);
    // Don't automatically reload - let user manually refresh or keep current problems
    toast({
      title: "Section Changed",
      description: "Click 'Reload Problems' to update the problem set"
    });
  };

  const handleReloadProblems = () => {
    loadProblemsFromSections();
    toast({
      title: "Problems Reloaded",
      description: `Loaded problems from: ${selectedSections.join(', ')}`
    });
  };

  const handleCursorChange = (position: { line: number; column: number }) => {
    console.log('Cursor moved to:', position);
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'Easy': return 'text-success';
      case 'Medium': return 'text-warning';
      case 'Hard': return 'text-destructive';
      default: return 'text-muted-foreground';
    }
  };

  const currentProblemData = problems[currentProblem];

  if (isJoining) {
    return (
      <div className="h-screen bg-background flex items-center justify-center">
        <div className="text-center space-y-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
          <h2 className="text-xl font-semibold">Joining Room...</h2>
          <p className="text-muted-foreground">Room Code: {roomCode}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="h-screen bg-background flex flex-col">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className={`p-2 rounded-lg ${isContest ? 'bg-warning/10' : 'bg-primary/10'}`}>
                {isContest ? <Trophy className="h-5 w-5 text-warning" /> : <Code className="h-5 w-5 text-primary" />}
              </div>
              <div>
                <h1 className="text-lg font-bold">{roomName || 'Coding Session'}</h1>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <span>Room: {roomCode}</span>
                  <Button 
                    variant="ghost" 
                    size="sm" 
                    onClick={copyRoomCode}
                    className="h-6 w-6 p-0"
                  >
                    <Copy className="h-3 w-3" />
                  </Button>
                </div>
              </div>
            </div>

            <div className="flex items-center gap-4">
              {/* Contest Timer - Individual per user */}
              {isContest && timeLeft !== null && (
                <div className="flex items-center gap-2 px-3 py-1 bg-warning/10 rounded-lg">
                  <Clock className="h-4 w-4 text-warning" />
                  <span className="font-mono text-warning font-semibold">
                    {formatTime(timeLeft)}
                  </span>
                  <span className="text-xs text-muted-foreground">Your time</span>
                </div>
              )}
              
              <div className="flex items-center gap-2">
                <Users className="h-4 w-4 text-muted-foreground" />
                <span className="text-sm">{participants.filter(p => p.isOnline).length} online</span>
              </div>

              {/* Global Problem Navigation */}
              {isAdmin && problems.length > 1 && (
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={handlePrevProblem}
                    disabled={currentProblem === 0 || loadingProblems}
                  >
                    Previous
                  </Button>
                  <span className="text-sm text-muted-foreground px-2">
                    {currentProblem + 1} / {problems.length}
                  </span>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={handleNextProblem}
                    disabled={currentProblem === problems.length - 1 || loadingProblems}
                  >
                    Next
                  </Button>
                </div>
              )}

              <Button 
                variant="outline" 
                size="sm"
                onClick={() => setShowSidebar(!showSidebar)}
                className="gap-2"
              >
                <Users className="h-4 w-4" />
                {!isContest && <MessageCircle className="h-4 w-4" />}
                {isContest ? 'People' : 'People & Chat'}
              </Button>

              <Button 
                variant="outline" 
                size="sm"
                onClick={() => navigate('/')}
              >
                Leave Room
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="flex-1 flex overflow-hidden">
        <ResizablePanelGroup direction="horizontal" className="flex-1">
          
          {/* Problem Panel */}
          <ResizablePanel defaultSize={25} minSize={20} maxSize={40}>
            <div className="h-full flex flex-col border-r border-border">
              {/* Tabs for different sections - Different for contest vs collab */}
              <Tabs defaultValue="description" className="h-full flex flex-col">
                <div className="border-b border-border">
                  <TabsList className={`w-full h-auto ${isContest ? 'grid-cols-2' : 'grid-cols-4'} grid`}>
                    <TabsTrigger value="description" className="text-xs">Description</TabsTrigger>
                    {!isContest && <TabsTrigger value="editorial" className="text-xs">Editorial</TabsTrigger>}
                    {!isContest && <TabsTrigger value="solutions" className="text-xs">Solutions</TabsTrigger>}
                    <TabsTrigger value="submissions" className="text-xs">Submissions</TabsTrigger>
                  </TabsList>
                </div>

                <TabsContent value="description" className="flex-1 flex flex-col m-0">
                  {/* Problem Header */}
                  <div className="p-4 border-b border-border">
                    <div className="flex items-center justify-between mb-3">
                      <div className="flex items-center gap-2">
                        {loadingProblems ? (
                          <div className="flex items-center gap-2">
                            <Loader2 className="h-4 w-4 animate-spin" />
                            <span className="text-sm">Loading problems...</span>
                          </div>
                        ) : (
                          <>
                            <h2 className="text-xl font-bold">{problems[currentProblem]?.title || 'No Problem Selected'}</h2>
                            <Badge 
                              variant="outline" 
                              className={getDifficultyColor(problems[currentProblem]?.difficulty || '')}
                            >
                              {problems[currentProblem]?.difficulty}
                            </Badge>
                            <Badge variant="secondary">
                              {problems[currentProblem]?.topic}
                            </Badge>
                          </>
                        )}
                      </div>
                      
                      {isAdmin && (
                        <div className="flex items-center gap-1">
                          <Crown className="h-4 w-4 text-warning" />
                          <span className="text-xs text-muted-foreground">Admin</span>
                        </div>
                      )}
                    </div>

                  </div>

                  {/* Problem Content */}
                  <div className="flex-1 p-4 overflow-y-auto space-y-6">
                    {loadingProblems ? (
                      <div className="flex items-center justify-center h-32">
                        <Loader2 className="h-8 w-8 animate-spin" />
                      </div>
                    ) : problems[currentProblem] ? (
                      <>
                        {/* Description */}
                        <div>
                          <h3 className="font-semibold mb-2">Description</h3>
                          <p className="text-sm leading-relaxed">{problems[currentProblem].description}</p>
                        </div>

                        {/* Examples */}
                        <div>
                          <h3 className="font-semibold mb-3">Examples</h3>
                          <div className="space-y-4">
                            {problems[currentProblem].examples?.map((example, index) => (
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
                        {problems[currentProblem].constraints && (
                          <div>
                            <h3 className="font-semibold mb-2">Constraints</h3>
                            <ul className="text-sm space-y-1">
                              {problems[currentProblem].constraints.map((constraint, index) => (
                                <li key={index} className="flex items-start gap-2">
                                  <span className="text-muted-foreground">•</span>
                                  <span>{constraint}</span>
                                </li>
                              ))}
                            </ul>
                          </div>
                        )}
                      </>
                    ) : (
                      <div className="text-center text-muted-foreground">
                        No problems available in selected sections
                      </div>
                    )}
                  </div>
                </TabsContent>

                {!isContest && (
                  <>
                    <TabsContent value="editorial" className="flex-1 p-4">
                      <div className="text-center text-muted-foreground">
                        Editorial coming soon...
                      </div>
                    </TabsContent>

                    <TabsContent value="solutions" className="flex-1 p-4">
                      <div className="text-center text-muted-foreground">
                        Solutions coming soon...
                      </div>
                    </TabsContent>
                  </>
                )}

                <TabsContent value="submissions" className="flex-1 p-4">
                  <div className="text-center text-muted-foreground">
                    Submissions coming soon...
                  </div>
                </TabsContent>
              </Tabs>
            </div>
          </ResizablePanel>

          <ResizableHandle withHandle />

          {/* Editor and Test Cases */}
          <ResizablePanel defaultSize={50} minSize={30}>
            <ResizablePanelGroup direction="vertical">
              
              {/* Code Editor */}
              <ResizablePanel defaultSize={70} minSize={40}>
                <div className="h-full flex flex-col">
                  <div className="p-4 border-b border-border">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Code className="h-5 w-5 text-primary" />
                        <h2 className="text-xl font-semibold">Code Editor</h2>
                        {loadingTemplate && (
                          <Loader2 className="h-4 w-4 animate-spin" />
                        )}
                      </div>
                      <div className="flex items-center gap-2">
                        <Select value={language} onValueChange={setLanguage}>
                          <SelectTrigger className="w-32">
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="python">Python</SelectItem>
                            <SelectItem value="javascript">JavaScript</SelectItem>
                            <SelectItem value="java">Java</SelectItem>
                          </SelectContent>
                        </Select>
                        <Button variant="outline" onClick={() => handleRunTests()}>
                          <Play className="h-4 w-4 mr-2" />
                          Run
                        </Button>
                        <Button onClick={handleSubmit}>
                          Submit
                        </Button>
                      </div>
                    </div>
                  </div>

                  <div className="flex-1">
                    <MonacoEditor
                      value={code}
                      onChange={setCode}
                      language="python"
                      theme="vs-dark"
                      height="100%"
                      onCursorChange={handleCursorChange}
                      roomId={roomCode}
                      userId="user-1"
                    />
                  </div>
                </div>
              </ResizablePanel>

              <ResizableHandle withHandle />

              {/* Test Cases Panel - Now Editable */}
              <ResizablePanel defaultSize={30} minSize={20}>
                <TestCasePanel
                  testCases={testCases}
                  onRunTests={handleRunTests}
                  onAddTestCase={(newCase) => {
                    const testCase = {
                      ...newCase,
                      id: Date.now().toString()
                    };
                    setTestCases(prev => [...prev, testCase]);
                    toast({
                      title: "Test Case Added",
                      description: "New test case has been added"
                    });
                  }}
                  onEditTestCase={(id, updates) => {
                    setTestCases(prev => prev.map(tc => 
                      tc.id === id ? { ...tc, ...updates } : tc
                    ));
                    toast({
                      title: "Test Case Updated",
                      description: "Test case has been updated"
                    });
                  }}
                  onDeleteTestCase={(id) => {
                    setTestCases(prev => prev.filter(tc => tc.id !== id));
                    toast({
                      title: "Test Case Deleted",
                      description: "Test case has been removed"
                    });
                  }}
                  isRunning={testCases.some(tc => tc.status === 'running')}
                />
              </ResizablePanel>

            </ResizablePanelGroup>
          </ResizablePanel>

          {/* Right Panel - Only when opened */}
          {showSidebar && (
            <>
              <ResizableHandle withHandle />
              <ResizablePanel defaultSize={25} minSize={15} maxSize={35}>
                <div className="h-full border-l border-border bg-card/30">
                  <RoomSidebar
                    participants={participants}
                    chatMessages={isContest ? [] : chatMessages}
                    leaderboard={leaderboard}
                    activeTab={isContest ? 'participants' : activeTab}
                    onTabChange={setActiveTab}
                    onSendMessage={isContest ? () => {} : handleSendMessage}
                    isOpen={showSidebar}
                    onClose={() => setShowSidebar(false)}
                    isContest={isContest}
                  />
                </div>
              </ResizablePanel>
            </>
          )}

        </ResizablePanelGroup>
      </div>
    </div>
  );
};

export default Room;