import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Badge } from '@/components/ui/badge';
import { ScrollArea } from '@/components/ui/scroll-area';
import { ArrowLeft, ArrowRight, Trophy, Clock, Users } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { ContestService, Topic, Problem } from '@/services/contestService';

const CreateContest = () => {
  const [contestName, setContestName] = useState('');
  const [description, setDescription] = useState('');
  const [duration, setDuration] = useState('');
  const [startTime, setStartTime] = useState('');
  const [maxParticipants, setMaxParticipants] = useState('50');
  const [topics, setTopics] = useState<Topic[]>([]);
  const [selectedTopic, setSelectedTopic] = useState<number | null>(null);
  const [problems, setProblems] = useState<Problem[]>([]);
  const [selectedProblems, setSelectedProblems] = useState<number[]>([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();
  
  // Get pre-selected problems from navigation state
  const location = useLocation();
  const preSelectedProblems = location.state?.selectedProblems || [];

  // Load topics on component mount
  useEffect(() => {
    const loadTopics = async () => {
      try {
        const topicsData = await ContestService.getTopics();
        setTopics(topicsData);
      } catch (error: any) {
        toast({
          title: "Error",
          description: error.message || "Failed to load topics",
          variant: "destructive"
        });
      }
    };

    loadTopics();
  }, [toast]);

  // Handle pre-selected problems from SelectProblems page
  useEffect(() => {
    if (preSelectedProblems.length > 0) {
      setSelectedProblems(preSelectedProblems);
      console.log('Pre-selected problems loaded:', preSelectedProblems);
      
      // Load problem details for display
      const loadPreSelectedProblems = async () => {
        try {
          const problemsData = await ContestService.loadSelectedProblems(preSelectedProblems);
          setProblems(problemsData);
          console.log('Pre-selected problem details loaded:', problemsData);
        } catch (error: any) {
          console.error('Failed to load pre-selected problem details:', error);
        }
      };
      
      loadPreSelectedProblems();
    }
  }, [preSelectedProblems]);

  // Load problems when topic is selected
  useEffect(() => {
    if (selectedTopic) {
      const loadProblems = async () => {
        try {
          const problemsData = await ContestService.getProblemsByTopic(selectedTopic);
          setProblems(problemsData);
        } catch (error: any) {
          toast({
            title: "Error",
            description: error.message || "Failed to load problems",
            variant: "destructive"
          });
        }
      };

      loadProblems();
    } else {
      setProblems([]);
    }
  }, [selectedTopic, toast]);

  const handleCreateContest = async () => {
    if (!contestName.trim() || !description.trim() || !duration || !startTime || selectedProblems.length === 0) {
      toast({
        title: "Missing Information",
        description: "Please fill in all contest details and select at least one problem",
        variant: "destructive"
      });
      return;
    }

    setLoading(true);

    try {
      const contestData = {
        title: contestName,
        description: description,
        startTime: startTime,
        duration: parseInt(duration),
        problemIds: selectedProblems,
        maxParticipants: parseInt(maxParticipants)
      };

      console.log('=== CREATE CONTEST COMPONENT DEBUG ===');
      console.log('Selected problems:', selectedProblems);
      console.log('Contest data being sent:', contestData);

      const contest = await ContestService.createContest(contestData);
      
      console.log('Contest created successfully:', contest);
      console.log('Contest ID:', contest.id);
      console.log('Contest problemIds:', contest.problemIds);
      
      toast({
        title: "Contest Created!",
        description: `Contest "${contest.title}" has been created successfully!`,
      });

      // Navigate to contest room
      console.log('Navigating to contest room:', `/contest/${contest.id}`);
      navigate(`/contest/${contest.id}`, { 
        state: { 
          contest,
          selectedProblems: selectedProblems
        } 
      });
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.message || "Failed to create contest",
        variant: "destructive"
      });
    } finally {
      setLoading(false);
    }
  };

  const handleProblemToggle = (problemId: number) => {
    setSelectedProblems(prev => 
      prev.includes(problemId) 
        ? prev.filter(id => id !== problemId)
        : [...prev, problemId]
    );
  };



  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center gap-4">
            <Button 
              variant="ghost" 
              onClick={() => navigate('/')}
              className="gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              Back to Home
            </Button>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-warning/10 rounded-lg">
                <Trophy className="h-5 w-5 text-warning" />
              </div>
              <div>
                <h1 className="text-xl font-bold">Create Coding Contest</h1>
                <p className="text-sm text-muted-foreground">Host a competitive coding competition</p>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="container mx-auto px-4 py-8">
        <div className="max-w-4xl mx-auto space-y-8">
          <ScrollArea className="h-[calc(100vh-200px)]">
            <div className="space-y-8 pr-4">
          
          {/* Contest Setup */}
          <Card className="bg-gradient-card border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Trophy className="h-5 w-5 text-warning" />
                Contest Configuration
              </CardTitle>
              <CardDescription>
                Set up your competitive coding contest with timing and rules
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <Label htmlFor="contestName">Contest Name</Label>
                  <Input
                    id="contestName"
                    placeholder="e.g., Weekly Algorithm Challenge"
                    value={contestName}
                    onChange={(e) => setContestName(e.target.value)}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="description">Description</Label>
                  <Input
                    id="description"
                    placeholder="Describe your contest..."
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="duration">Duration (minutes)</Label>
                  <Select onValueChange={setDuration}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select duration" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="30">30 minutes</SelectItem>
                      <SelectItem value="60">1 hour</SelectItem>
                      <SelectItem value="90">1.5 hours</SelectItem>
                      <SelectItem value="120">2 hours</SelectItem>
                      <SelectItem value="180">3 hours</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="startTime">Start Time</Label>
                  <Select onValueChange={setStartTime}>
                    <SelectTrigger>
                      <SelectValue placeholder="When to start" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="5min">In 5 minutes</SelectItem>
                      <SelectItem value="15min">In 15 minutes</SelectItem>
                      <SelectItem value="30min">In 30 minutes</SelectItem>
                      <SelectItem value="1hour">In 1 hour</SelectItem>
                      <SelectItem value="2hour">In 2 hours</SelectItem>
                      <SelectItem value="tomorrow">Tomorrow</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="maxParticipants">Max Participants</Label>
                  <Input
                    id="maxParticipants"
                    type="number"
                    placeholder="50"
                    value={maxParticipants}
                    onChange={(e) => setMaxParticipants(e.target.value)}
                  />
                </div>

                <div className="space-y-2">
                  <Label className="text-sm font-medium">Selected Problems</Label>
                  <div className="flex flex-wrap gap-2">
                    {selectedProblems.length > 0 ? (
                      selectedProblems.map(id => {
                        const problem = problems.find(p => p.id === id);
                        return (
                          <Badge key={id} variant="secondary" className="cursor-pointer" onClick={() => handleProblemToggle(id)}>
                            {problem?.title || `Problem ${id}`} ×
                          </Badge>
                        );
                      })
                    ) : (
                      <span className="text-muted-foreground text-sm">No problems selected</span>
                    )}
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Topic Selection */}
          <Card className="bg-gradient-card border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Trophy className="h-5 w-5 text-warning" />
                Select Topic
              </CardTitle>
              <CardDescription>
                Choose a topic to browse available problems
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Select onValueChange={(value) => setSelectedTopic(parseInt(value))}>
                <SelectTrigger>
                  <SelectValue placeholder="Select a topic" />
                </SelectTrigger>
                <SelectContent>
                  {topics.map(topic => (
                    <SelectItem key={topic.topicId} value={topic.topicId.toString()}>
                      {topic.topicName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </CardContent>
          </Card>

          {/* Problem Selection */}
          {(selectedTopic || preSelectedProblems.length > 0) && (
            <Card className="bg-gradient-card border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Trophy className="h-5 w-5 text-warning" />
                  {preSelectedProblems.length > 0 ? 'Selected Problems' : 'Select Problems'}
                </CardTitle>
                <CardDescription>
                  {preSelectedProblems.length > 0 
                    ? `${preSelectedProblems.length} problem${preSelectedProblems.length > 1 ? 's' : ''} selected for your contest`
                    : `Choose problems for your contest from ${topics.find(t => t.topicId === selectedTopic)?.topicName}`
                  }
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="grid gap-3">
                  {problems.map(problem => (
                    <div
                      key={problem.id}
                      className={`p-4 border rounded-lg cursor-pointer transition-colors ${
                        selectedProblems.includes(problem.id)
                          ? 'border-warning bg-warning/10'
                          : 'border-border hover:border-warning/50'
                      }`}
                      onClick={() => handleProblemToggle(problem.id)}
                    >
                      <div className="flex items-center justify-between">
                        <div>
                          <h3 className="font-medium">{problem.title}</h3>
                          <p className="text-sm text-muted-foreground">ID: {problem.id}</p>
                        </div>
                        <Badge variant={problem.difficulty === 'EASY' ? 'default' : problem.difficulty === 'MEDIUM' ? 'secondary' : 'destructive'}>
                          {problem.difficulty}
                        </Badge>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          )}

            </div>
          </ScrollArea>
        </div>
      </div>

      {/* Sticky Create Contest Button */}
      <div className="fixed bottom-0 left-0 right-0 bg-background border-t border-border p-4 z-50">
        <div className="container mx-auto max-w-4xl">
          <Button 
            onClick={handleCreateContest}
            className="w-full bg-warning hover:bg-warning/90 text-warning-foreground"
            disabled={!contestName.trim() || !description.trim() || !duration || !startTime || selectedProblems.length === 0 || loading}
          >
            {loading ? 'Creating Contest...' : 'Create Contest'}
            <ArrowRight className="ml-2 h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  );
};

export default CreateContest;