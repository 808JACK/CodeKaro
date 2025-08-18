import { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { ScrollArea } from '@/components/ui/scroll-area';
import {
  Code,
  Users,
  Play,
  Trophy,
  Clock,
  Menu,
  Crown,
  CheckCircle,
  XCircle,
  Loader2,
  ArrowLeft,
  Calendar,
  Timer,
  UserCheck
} from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { Badge } from '@/components/ui/badge';
import { ContestService, ContestDetails, ProblemDetails, CodeExecutionRequest, CodeExecutionResponse, TestResult } from '@/services/contestService';
import { ContestMonacoEditor } from '@/components/contest/ContestMonacoEditor';
import { ContestStartInterface } from '@/components/contest/ContestStartInterface';

interface LocationState {
  contest: ContestDetails;
  selectedProblems: number[];
}

interface TestCase {
  id: string;
  inputs: { name: string; value: string }[];
  expectedOutput: string;
  actualOutput?: string;
  status?: 'passed' | 'failed' | 'running' | 'pending' | 'error';
  runtime?: number;
  error?: string;
  verdict?: string;
}

const ContestRoom = () => {
  console.log('=== CONTEST ROOM COMPONENT LOADED ===');
  console.log('Component rendered at:', new Date().toISOString());

  const { contestId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { toast } = useToast();

  const [contest, setContest] = useState<ContestDetails | null>(null);
  const [problems, setProblems] = useState<ProblemDetails[]>([]);
  const [currentProblemIndex, setCurrentProblemIndex] = useState(0);
  const [timeLeft, setTimeLeft] = useState<number>(0);
  const [loading, setLoading] = useState(true);
  const [code, setCode] = useState<string>('');
  const [language, setLanguage] = useState('python');
  const [testCases, setTestCases] = useState<TestCase[]>([]);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('description');
  const [isAdmin, setIsAdmin] = useState(false);
  const [isRunning, setIsRunning] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [contestStarted, setContestStarted] = useState(false);
  const [showStartInterface, setShowStartInterface] = useState(true);

  // Get contest data from location state or fetch from API
  const state = location.state as LocationState;

  useEffect(() => {
    console.log('=== CONTEST ROOM USE_EFFECT TRIGGERED ===');
    console.log('useEffect dependencies changed:', { contestId, state });

    const loadContestData = async () => {
      try {
        console.log('=== CONTEST ROOM DEBUG ===');
        console.log('Location state:', state);
        console.log('Contest ID from params:', contestId);
        console.log('State contest:', state?.contest);
        console.log('State selectedProblems:', state?.selectedProblems);

        let contestData: ContestDetails;
        let problemIds: number[];

        if (state?.contest) {
          // Creator joining - use data from state
          contestData = state.contest;
          problemIds = state.selectedProblems || contestData.problemIds;
          console.log('Creator flow - contest data:', contestData);
          console.log('Creator flow - problem IDs:', problemIds);
          setContest(contestData);

          // Creator is always admin
          setIsAdmin(true);
          console.log('Creator joining - user is admin');
        } else if (contestId) {
          // Joiner joining - first join the contest, then fetch details
          console.log('Joining contest:', contestId);

          try {
            // First try to join the contest - this will fail if contest doesn't exist
            console.log('Attempting to join contest...');
            const joinResult = await ContestService.joinContest(contestId);
            console.log('Join contest result:', joinResult);

            // The join result should contain contest details including problem IDs
            // If not, fetch contest details separately
            if (joinResult.problemIds && joinResult.problemIds.length > 0) {
              // Join result has problem IDs - use it directly
              contestData = {
                id: contestId,
                title: joinResult.name || joinResult.title || 'Contest',
                description: joinResult.description || '',
                startTime: joinResult.startTime,
                endTime: new Date(new Date(joinResult.startTime).getTime() + joinResult.durationMinutes * 60 * 1000).toISOString(),
                duration: joinResult.durationMinutes,
                problemIds: joinResult.problemIds,
                maxParticipants: joinResult.maxParticipants || 50,
                currentParticipants: joinResult.currentParticipants || 1,
                status: 'ONGOING' as const,
                createdBy: joinResult.createdBy || 0
              };
              problemIds = joinResult.problemIds;
              console.log('Using contest data from join result:', contestData);
            } else {
              // Join result doesn't have problem IDs - fetch contest details
              console.log('Fetching contest details...');
              contestData = await ContestService.getContestDetails(contestId);
              problemIds = contestData.problemIds;
              console.log('Contest details fetched:', contestData);
            }

            setContest(contestData);

            // Check if current user is admin
            const currentUserId = parseInt(localStorage.getItem('userId') || '0');
            const userIsAdmin = contestData.createdBy === currentUserId;
            setIsAdmin(userIsAdmin);
            console.log('User admin status:', { currentUserId, createdBy: contestData.createdBy, isAdmin: userIsAdmin });

            console.log('Contest data loaded successfully:', contestData);
            console.log('Problem IDs found:', problemIds);
          } catch (joinError: any) {
            console.error('Failed to join contest:', joinError);
            
            // If contest doesn't exist, try to join as a regular room instead
            if (joinError.message.includes('Contest with code') || joinError.message.includes('does not exist')) {
              console.log('Contest not found, attempting to join as regular room...');
              
              // Redirect to regular room
              navigate(`/room/${contestId}`, { replace: true });
              return; // Exit early to prevent further execution
            } else {
              throw new Error(`Failed to join contest: ${joinError.message}`);
            }
          }
        } else {
          throw new Error('No contest data available');
        }

        // Load all problems at once - same for both creator and joiner
        if (problemIds && problemIds.length > 0) {
          console.log('Loading problems:', problemIds);
          try {
            const problemsData = await ContestService.loadSelectedProblems(problemIds);
            console.log('Problems loaded successfully:', problemsData);
            setProblems(problemsData);

            // Set initial empty code (no template)
            if (problemsData.length > 0) {
              const firstProblem = problemsData[0];
              console.log('Setting initial problem:', firstProblem);
              setCode('# Write your solution here\n');

              // Add test cases from the problem to the test case panel
              if (firstProblem.examples && firstProblem.examples.length > 0) {
                const problemTestCases: TestCase[] = firstProblem.examples.map((example, index) => ({
                  id: `test-${index}`,
                  inputs: [
                    { name: 'Input', value: example.input }
                  ],
                  expectedOutput: example.output,
                  status: 'pending' as any
                }));
                setTestCases(problemTestCases);
                console.log('Test cases loaded:', problemTestCases);
              } else {
                console.log('No examples found for first problem');
                setTestCases([]);
              }
            } else {
              console.log('No problems data received');
              setCode('# Write your solution here\n');
              setTestCases([]);
            }
          } catch (problemError: any) {
            console.error('Failed to load problems:', problemError);
            console.error('Problem error details:', {
              message: problemError.message,
              stack: problemError.stack,
              problemIds: problemIds
            });
            throw new Error(`Failed to load contest problems: ${problemError.message}`);
          }
        } else {
          console.error('No problem IDs found in contest data');
          console.error('Contest data debug:', {
            contestData,
            problemIds,
            state: state,
            contestId: contestId
          });
          throw new Error('No problems found in contest');
        }

        // Calculate time left - same for both creator and joiner
        const startTime = new Date(contestData.startTime).getTime();
        const endTime = startTime + (contestData.duration * 60 * 1000);
        const now = Date.now();
        const remaining = Math.max(0, endTime - now);
        console.log('Time calculation:', {
          startTime: new Date(startTime).toISOString(),
          endTime: new Date(endTime).toISOString(),
          now: new Date(now).toISOString(),
          duration: contestData.duration,
          remaining: remaining
        });
        setTimeLeft(remaining);

      } catch (error: any) {
        console.error('Error loading contest data:', error);
        toast({
          title: "Error",
          description: error.message || "Failed to load contest",
          variant: "destructive"
        });
        navigate('/');
      } finally {
        setLoading(false);
      }
    };

    loadContestData();
  }, [contestId, state, navigate, toast]);

  // Timer countdown
  useEffect(() => {
    if (timeLeft <= 0) return;

    const timer = setInterval(() => {
      setTimeLeft(prev => {
        if (prev <= 1000) {
          toast({
            title: "Contest Ended",
            description: "Time's up! Contest has ended.",
          });
          return 0;
        }
        return prev - 1000;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [timeLeft, toast]);

  // Check if contest should start automatically
  useEffect(() => {
    if (contest) {
      const startTime = new Date(contest.startTime).getTime();
      const now = Date.now();
      
      // If contest start time has passed, start automatically
      if (now >= startTime) {
        setContestStarted(true);
        setShowStartInterface(false);
      }
    }
  }, [contest]);

  const handleStartContest = () => {
    setContestStarted(true);
    setShowStartInterface(false);
    toast({
      title: "Contest Started!",
      description: "Good luck with your coding challenge!",
    });
  };

  const formatTime = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const handleProblemChange = (index: number) => {
    setCurrentProblemIndex(index);
    setCode('# Write your solution here\n'); // No template, start fresh

    // Update test cases for the new problem
    const newProblem = problems[index];
    const problemTestCases: TestCase[] = newProblem.examples.map((example, idx) => ({
      id: `test-${idx}`,
      inputs: [
        { name: 'Input', value: example.input }
      ],
      expectedOutput: example.output,
      status: 'pending' as any
    }));
    setTestCases(problemTestCases);
  };

  const handleRunTests = async () => {
    if (!code.trim()) {
      toast({
        title: "Error",
        description: "Please write some code before running tests.",
        variant: "destructive"
      });
      return;
    }

    // Quick health check
    console.log('=== PRE-EXECUTION HEALTH CHECK ===');
    console.log('Backend URL:', 'http://localhost:8092');
    console.log('Current time:', new Date().toISOString());
    console.log('Code length:', code.length, 'characters');
    console.log('Language:', language);
    console.log('Problem:', currentProblem.title, '(ID:', currentProblem.id, ')');

    setIsRunning(true);

    // Set all test cases to running state
    setTestCases(prev => prev.map(tc => ({ ...tc, status: 'running' as any })));

    try {
      // Choose which identifier to use for MinIO file lookup
      const problemName = currentProblem.title; // Using title (e.g., "Two Sum")
      // Alternative options:
      // const problemName = currentProblem.id.toString(); // Using ID (e.g., "1")
      // const problemName = currentProblem.title.toLowerCase().replace(/\s+/g, '-'); // Using normalized title (e.g., "two-sum")

      console.log('=== CODE EXECUTION DEBUG ===');
      console.log('Problem ID:', currentProblem.id);
      console.log('Problem Title:', currentProblem.title);
      console.log('Problem Name being sent:', problemName);
      console.log('Expected MinIO file:', `${problemName}.txt`);

      const request: CodeExecutionRequest = {
        code: code,
        language: language,
        problemId: currentProblem.id, // Add problemId for consistency
        problemName: problemName,
        timeLimitMs: 2000, // 2 seconds per test
        memoryLimitMb: 256 // 256 MB
      };

      console.log('Code execution request:', request);
      console.log('Code being executed:');
      console.log('---START CODE---');
      console.log(code);
      console.log('---END CODE---');

      toast({
        title: "Executing Code...",
        description: `Running tests for ${currentProblem.title}`,
      });

      const result = await ContestService.runCode(request);
      console.log('Code execution result:', result);

      // Log detailed execution results
      console.log('=== EXECUTION RESULTS ANALYSIS ===');
      console.log('Overall verdict:', result.overallVerdict);
      console.log('Total test results:', result.testResults?.length || 0);
      console.log('Docker execution successful:', result.overallVerdict !== 'CE');

      if (result.error) {
        console.error('Execution error:', result.error);
      }

      // Update test cases with results
      const updatedTestCases = testCases.map((testCase, index) => {
        const testResult = result.testResults.find(tr => tr.testNumber === index + 1);

        if (testResult) {
          return {
            ...testCase,
            status: testResult.verdict === 'AC' ? 'passed' : 'failed',
            actualOutput: testResult.output,
            runtime: testResult.timeMs,
            error: testResult.error,
            verdict: testResult.verdict
          };
        }

        return {
          ...testCase,
          status: 'error' as any,
          error: 'No result found'
        };
      });

      setTestCases(updatedTestCases);

      // Show overall result
      const passedTests = updatedTestCases.filter(tc => tc.status === 'passed').length;
      const totalTests = updatedTestCases.length;

      if (result.overallVerdict === 'AC') {
        toast({
          title: "All Tests Passed! ✅",
          description: `${passedTests}/${totalTests} test cases passed`,
        });
      } else {
        toast({
          title: "Some Tests Failed ❌",
          description: `${passedTests}/${totalTests} test cases passed`,
          variant: "destructive"
        });
      }

    } catch (error: any) {
      console.error('=== CODE EXECUTION ERROR ===');
      console.error('Error type:', error.constructor.name);
      console.error('Error message:', error.message);
      console.error('Full error:', error);

      // Check for common Docker issues
      let errorDescription = error.message || "Failed to run tests";
      let debugHint = "";

      if (error.message?.includes('Failed to fetch') || error.message?.includes('NetworkError')) {
        debugHint = "Check if Collab Service is running on http://localhost:8092";
        errorDescription = "Cannot connect to backend service";
      } else if (error.message?.includes('Docker not available')) {
        debugHint = "Start Docker Desktop and restart Collab Service";
        errorDescription = "Docker is not running";
      } else if (error.message?.includes('Failed to get Docker container')) {
        debugHint = "Check Docker container creation in backend logs";
        errorDescription = "Docker container creation failed";
      } else if (error.message?.includes('No Test Cases')) {
        debugHint = `Check if "${currentProblem.title}.txt" or "${currentProblem.title}" exists in MinIO testcases bucket`;
        errorDescription = "Test case file not found in MinIO";
      }

      console.log('Debug hint:', debugHint);

      // Set all test cases to error state
      setTestCases(prev => prev.map(tc => ({
        ...tc,
        status: 'error' as any,
        error: error.message || 'Execution failed'
      })));

      toast({
        title: "Execution Error",
        description: debugHint || errorDescription,
        variant: "destructive"
      });
    } finally {
      setIsRunning(false);
    }
  };

  const handleSubmit = async () => {
    if (!code.trim()) {
      toast({
        title: "Error",
        description: "Please write some code before submitting.",
        variant: "destructive"
      });
      return;
    }

    setIsSubmitting(true);

    try {
      // Choose which identifier to use for MinIO file lookup
      const problemName = currentProblem.title; // Using title (e.g., "Two Sum")
      // Alternative options:
      // const problemName = currentProblem.id.toString(); // Using ID (e.g., "1")
      // const problemName = currentProblem.title.toLowerCase().replace(/\s+/g, '-'); // Using normalized title (e.g., "two-sum")

      console.log('=== CODE SUBMISSION DEBUG ===');
      console.log('Problem ID:', currentProblem.id);
      console.log('Problem Title:', currentProblem.title);
      console.log('Problem Name being sent:', problemName);
      console.log('Expected MinIO file:', `${problemName}.txt`);

      const request: CodeExecutionRequest = {
        code: code,
        language: language,
        problemId: currentProblem.id, // CRITICAL: Add problemId for MongoDB saving
        problemName: problemName,
        timeLimitMs: 2000, // 2 seconds per test
        memoryLimitMb: 256 // 256 MB
      };

      console.log('Code submission request:', request);
      console.log('Code being submitted:');
      console.log('---START CODE---');
      console.log(code);
      console.log('---END CODE---');

      toast({
        title: "Submitting Code...",
        description: `Running all tests for ${currentProblem.title}`,
      });

      const result = await ContestService.submitCode(request, contestId, contest?.startTime);
      console.log('Code submission result:', result);

      // Log detailed submission results
      console.log('=== SUBMISSION RESULTS ANALYSIS ===');
      console.log('Overall verdict:', result.overallVerdict);
      console.log('Total test results:', result.testResults?.length || 0);
      console.log('Docker execution successful:', result.overallVerdict !== 'CE');

      if (result.error) {
        console.error('Submission error:', result.error);
      }

      if (result.overallVerdict === 'AC') {
        toast({
          title: "Submission Accepted! 🎉",
          description: "All test cases passed successfully!",
        });
      } else {
        const passedTests = result.testResults.filter(tr => tr.verdict === 'AC').length;
        const totalTests = result.testResults.length;

        toast({
          title: "Submission Failed",
          description: `${passedTests}/${totalTests} test cases passed`,
          variant: "destructive"
        });
      }

    } catch (error: unknown) {
      console.error('=== CODE SUBMISSION ERROR ===');
      console.error('Error type:', error.constructor.name);
      console.error('Error message:', error.message);
      console.error('Full error:', error);

      // Check for common Docker issues
      let errorDescription = error.message || "Failed to submit code";
      let debugHint = "";

      if (error.message?.includes('Failed to fetch') || error.message?.includes('NetworkError')) {
        debugHint = "Check if Collab Service is running on http://localhost:8092";
        errorDescription = "Cannot connect to backend service";
      } else if (error.message?.includes('Docker not available')) {
        debugHint = "Start Docker Desktop and restart Collab Service";
        errorDescription = "Docker is not running";
      } else if (error.message?.includes('Failed to get Docker container')) {
        debugHint = "Check Docker container creation in backend logs";
        errorDescription = "Docker container creation failed";
      } else if (error.message?.includes('No Test Cases')) {
        debugHint = `Check if "${currentProblem.title}.txt" or "${currentProblem.title}" exists in MinIO testcases bucket`;
        errorDescription = "Test case file not found in MinIO";
      }

      console.log('Debug hint:', debugHint);

      toast({
        title: "Submission Error",
        description: debugHint || errorDescription,
        variant: "destructive"
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleNextProblem = () => {
    if (currentProblemIndex < problems.length - 1) {
      handleProblemChange(currentProblemIndex + 1);
    }
  };

  const handlePrevProblem = () => {
    if (currentProblemIndex > 0) {
      handleProblemChange(currentProblemIndex - 1);
    }
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'EASY': return 'bg-green-500';
      case 'MEDIUM': return 'bg-yellow-500';
      case 'HARD': return 'bg-red-500';
      default: return 'bg-gray-500';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="h-8 w-8 animate-spin mx-auto mb-4" />
          <p>Loading contest...</p>
        </div>
      </div>
    );
  }

  if (!contest || problems.length === 0) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <p>Contest not found or no problems available.</p>
          <Button onClick={() => navigate('/', { state: { fromContest: true } })} className="mt-4">
            Back to Home
          </Button>
        </div>
      </div>
    );
  }

  const currentProblem = problems[currentProblemIndex];

  // Contest Starting Interface
  if (showStartInterface && !contestStarted && contest) {
    return (
      <ContestStartInterface
        contest={contest}
        problems={problems}
        isAdmin={isAdmin}
        onStartContest={handleStartContest}
        onBack={() => navigate('/', { state: { fromContest: true } })}
      />
    );
  }

  return (
    <div className="h-screen bg-background flex flex-col">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm">
        <div className="flex items-center justify-between px-4 py-3">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              onClick={() => navigate('/', { state: { fromContest: true } })}
              className="gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              Back
            </Button>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-warning/10 rounded-lg">
                <Trophy className="h-5 w-5 text-warning" />
              </div>
              <div>
                <h1 className="text-lg font-bold">{contest.title}</h1>
                <p className="text-sm text-muted-foreground">Room: {contest.id}</p>
              </div>
              <div className="flex items-center gap-1 text-sm text-muted-foreground">
                <Crown className="h-4 w-4" />
                <span>{isAdmin ? 'Admin' : 'Participant'}</span>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2 text-warning font-mono text-lg">
              <Clock className="h-5 w-5" />
              {formatTime(Math.floor(timeLeft / 1000))}
            </div>
            <div className="flex items-center gap-2">
              <Button variant="outline" size="sm" onClick={handlePrevProblem} disabled={currentProblemIndex === 0}>
                Previous
              </Button>
              <span className="text-sm">
                {currentProblemIndex + 1}/{problems.length}
              </span>
              <Button variant="outline" size="sm" onClick={handleNextProblem} disabled={currentProblemIndex === problems.length - 1}>
                Next
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="flex-1 flex">
        <ResizablePanelGroup direction="horizontal">
          {/* Left Panel - Problem Description */}
          <ResizablePanel defaultSize={40} minSize={30}>
            <div className="h-full flex flex-col">
              <Tabs value={activeTab} onValueChange={setActiveTab} className="flex-1 flex flex-col">
                <TabsList className="grid w-full grid-cols-2 m-2">
                  <TabsTrigger value="description">Description</TabsTrigger>
                  <TabsTrigger value="submissions">Submissions</TabsTrigger>
                </TabsList>

                <TabsContent value="description" className="flex-1 m-0">
                  <ScrollArea className="h-full">
                    <div className="p-4 space-y-4">
                      <div className="flex items-center justify-between">
                        <h2 className="text-xl font-bold">{currentProblem.title}</h2>
                        <div className="flex items-center gap-2">
                          <Badge variant="outline">{currentProblem.topicIds.join(', ')}</Badge>
                          <Badge className={getDifficultyColor(currentProblem.difficulty)}>
                            {currentProblem.difficulty}
                          </Badge>
                        </div>
                      </div>

                      <div className="prose prose-sm max-w-none">
                        <div dangerouslySetInnerHTML={{ __html: currentProblem.content.replace(/\n/g, '<br/>') }} />
                      </div>

                      <div>
                        <h3 className="font-semibold mb-2">Constraints:</h3>
                        <pre className="bg-muted p-3 rounded text-sm whitespace-pre-wrap">
                          {currentProblem.constraints}
                        </pre>
                      </div>

                      <div className="grid md:grid-cols-2 gap-4">
                        <div>
                          <h3 className="font-semibold mb-2">Input Format:</h3>
                          <pre className="bg-muted p-3 rounded text-sm whitespace-pre-wrap">
                            {currentProblem.inputFormat}
                          </pre>
                        </div>
                        <div>
                          <h3 className="font-semibold mb-2">Output Format:</h3>
                          <pre className="bg-muted p-3 rounded text-sm whitespace-pre-wrap">
                            {currentProblem.outputFormat}
                          </pre>
                        </div>
                      </div>

                      <div>
                        <h3 className="font-semibold mb-2">Examples:</h3>
                        <div className="space-y-3">
                          {currentProblem.examples.map((example, index) => (
                            <div key={index} className="bg-muted border rounded p-3">
                              <div className="space-y-2">
                                <div>
                                  <h4 className="font-medium text-sm mb-1">Input:</h4>
                                  <pre className="text-xs whitespace-pre-wrap">
                                    {example.input}
                                  </pre>
                                </div>
                                <div>
                                  <h4 className="font-medium text-sm mb-1">Output:</h4>
                                  <pre className="text-xs whitespace-pre-wrap">
                                    {example.output}
                                  </pre>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>
                  </ScrollArea>
                </TabsContent>

                <TabsContent value="submissions" className="flex-1 m-0">
                  <ScrollArea className="h-full">
                    <div className="p-4">
                      <div className="text-center text-muted-foreground py-8">
                        <XCircle className="h-12 w-12 mx-auto mb-4 opacity-50" />
                        <p>No submissions yet</p>
                        <p className="text-sm">Submit your first solution to see it here</p>
                      </div>
                    </div>
                  </ScrollArea>
                </TabsContent>
              </Tabs>
            </div>
          </ResizablePanel>

          <ResizableHandle />

          {/* Right Panel - Code Editor and Test Cases */}
          <ResizablePanel defaultSize={60} minSize={40}>
            <ResizablePanelGroup direction="vertical">
              {/* Code Editor Section */}
              <ResizablePanel defaultSize={70} minSize={40}>
                <div className="h-full flex flex-col">
                  {/* Code Editor Header */}
                  <div className="border-b border-border p-4">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <h3 className="font-semibold flex items-center gap-2">
                          <Code className="h-4 w-4" />
                          Code Editor
                        </h3>
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
                      </div>
                      <div className="flex items-center gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={handleRunTests}
                          disabled={isRunning || isSubmitting}
                        >
                          {isRunning ? (
                            <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                          ) : (
                            <Play className="h-4 w-4 mr-2" />
                          )}
                          {isRunning ? 'Running...' : 'Run'}
                        </Button>
                        <Button
                          size="sm"
                          onClick={handleSubmit}
                          disabled={isRunning || isSubmitting}
                        >
                          {isSubmitting ? (
                            <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                          ) : (
                            <CheckCircle className="h-4 w-4 mr-2" />
                          )}
                          {isSubmitting ? 'Submitting...' : 'Submit'}
                        </Button>
                      </div>
                    </div>
                  </div>

                  {/* Code Editor with Scroll */}
                  <div className="flex-1 overflow-hidden">
                    <ContestMonacoEditor
                      value={code}
                      onChange={setCode}
                      language={language}
                      theme="vs-dark"
                      readOnly={false}
                      showLineNumbers={true}
                      showMinimap={true}
                      fontSize={14}
                      wordWrap="on"
                      automaticLayout={true}
                    />
                  </div>
                </div>
              </ResizablePanel>

              <ResizableHandle />

              {/* Test Cases Section */}
              <ResizablePanel defaultSize={30} minSize={20}>
                <div className="h-full flex flex-col">
                  <div className="border-b border-border p-4">
                    <div className="flex items-center justify-between">
                      <h3 className="font-semibold">Test Cases</h3>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={handleRunTests}
                        disabled={isRunning || isSubmitting}
                      >
                        {isRunning ? (
                          <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                        ) : (
                          <Play className="h-4 w-4 mr-2" />
                        )}
                        {isRunning ? 'Running...' : 'Run All Tests'}
                      </Button>
                    </div>
                  </div>
                  
                  <ScrollArea className="flex-1">
                    <div className="p-4 space-y-3">
                      {testCases.map((testCase, index) => (
                        <div key={testCase.id} className="border rounded p-3">
                          <div className="flex items-center justify-between mb-2">
                            <div className="flex items-center gap-2">
                              <h4 className="font-medium text-sm">Test Case {index + 1}</h4>
                              {testCase.status === 'passed' && (
                                <CheckCircle className="h-4 w-4 text-green-500" />
                              )}
                              {testCase.status === 'failed' && (
                                <XCircle className="h-4 w-4 text-red-500" />
                              )}
                              {testCase.status === 'running' && (
                                <Loader2 className="h-4 w-4 animate-spin text-blue-500" />
                              )}
                              {testCase.status === 'error' && (
                                <XCircle className="h-4 w-4 text-red-500" />
                              )}
                            </div>
                            <div className="flex items-center gap-2">
                              {testCase.runtime && (
                                <span className="text-xs text-muted-foreground">
                                  {testCase.runtime}ms
                                </span>
                              )}
                              <Badge
                                variant={
                                  testCase.status === 'passed' ? 'default' :
                                    testCase.status === 'failed' ? 'destructive' :
                                      testCase.status === 'running' ? 'secondary' :
                                        testCase.status === 'error' ? 'destructive' :
                                          'outline'
                                }
                              >
                                {testCase.verdict || testCase.status || 'pending'}
                              </Badge>
                            </div>
                          </div>

                          <div className="grid md:grid-cols-2 gap-4 text-xs">
                            <div>
                              <h5 className="font-medium mb-1">Input:</h5>
                              <pre className="bg-muted p-2 rounded whitespace-pre-wrap">
                                {testCase.inputs[0]?.value}
                              </pre>
                            </div>
                            <div>
                              <h5 className="font-medium mb-1">Expected Output:</h5>
                              <pre className="bg-muted p-2 rounded whitespace-pre-wrap">
                                {testCase.expectedOutput}
                              </pre>
                            </div>
                          </div>

                          {testCase.actualOutput && (
                            <div className="mt-3">
                              <h5 className="font-medium mb-1 text-xs">Your Output:</h5>
                              <pre className={`p-2 rounded whitespace-pre-wrap text-xs ${testCase.status === 'passed' ? 'bg-green-50 border border-green-200' :
                                'bg-red-50 border border-red-200'
                                }`}>
                                {testCase.actualOutput}
                              </pre>
                            </div>
                          )}

                          {testCase.error && (
                            <div className="mt-3">
                              <h5 className="font-medium mb-1 text-xs text-red-600">Error:</h5>
                              <pre className="bg-red-50 border border-red-200 p-2 rounded whitespace-pre-wrap text-xs text-red-700">
                                {testCase.error}
                              </pre>
                            </div>
                          )}
                        </div>
                      ))}
                    </div>
                  </ScrollArea>
                </div>
              </ResizablePanel>
            </ResizablePanelGroup>
          </ResizablePanel>
        </ResizablePanelGroup>
      </div>
    </div>
  );
};

export default ContestRoom;
