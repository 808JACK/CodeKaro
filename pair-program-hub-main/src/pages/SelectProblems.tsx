import { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Check, Plus, Play, ArrowRight, ArrowLeft, Trophy } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { ContestService, Topic, Problem } from '@/services/contestService';

const SelectProblems = () => {
  const { roomCode } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { toast } = useToast();
  
  const [selectedProblems, setSelectedProblems] = useState<string[]>([]);
  const [activeCategory, setActiveCategory] = useState<string>('all');
  const [topics, setTopics] = useState<Topic[]>([]);
  const [problems, setProblems] = useState<Problem[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedTopic, setSelectedTopic] = useState<number | null>(null);
  
  const { roomName, contestName, isContest, selectedCategory } = location.state || {};

  // Load all topics on component mount
  useEffect(() => {
    const loadTopics = async () => {
      try {
        setLoading(true);
        const topicsData = await ContestService.getTopics();
        setTopics(topicsData);
        console.log('Topics loaded:', topicsData);
      } catch (error: any) {
        toast({
          title: "Error",
          description: error.message || "Failed to load topics",
          variant: "destructive"
        });
      } finally {
        setLoading(false);
      }
    };

    loadTopics();
  }, [toast]);

  // Load problems when topic is selected
  useEffect(() => {
    if (selectedTopic) {
      const loadProblems = async () => {
        try {
          setLoading(true);
          const problemsData = await ContestService.getProblemsByTopic(selectedTopic);
          setProblems(problemsData);
          console.log('Problems loaded for topic', selectedTopic, ':', problemsData);
        } catch (error: any) {
          toast({
            title: "Error",
            description: error.message || "Failed to load problems",
            variant: "destructive"
          });
        } finally {
          setLoading(false);
        }
      };

      loadProblems();
    } else {
      setProblems([]);
    }
  }, [selectedTopic, toast]);

  // Get unique categories from loaded problems
  const categories = ['all', ...Array.from(new Set(problems.map(p => p.difficulty?.toLowerCase() || 'unknown')))];
  
  // Filter problems based on active category
  const filteredProblems = activeCategory === 'all' 
    ? problems 
    : problems.filter(p => p.difficulty?.toLowerCase() === activeCategory);

  // Group problems by difficulty for display
  const problemsByDifficulty = categories.reduce((acc, category) => {
    if (category === 'all') return acc;
    acc[category] = problems.filter(p => p.difficulty?.toLowerCase() === category);
    return acc;
  }, {} as Record<string, Problem[]>);

  useEffect(() => {
    // If a specific category was pre-selected, set it as active
    if (selectedCategory && categories.includes(selectedCategory)) {
      setActiveCategory(selectedCategory);
    }
  }, [selectedCategory, categories]);

  const handleProblemToggle = (problemId: string) => {
    setSelectedProblems(prev => 
      prev.includes(problemId)
        ? prev.filter(id => id !== problemId)
        : [...prev, problemId]
    );
  };

  const handleSelectAllInCategory = (category: string) => {
    const categoryProblems = category === 'all' ? problems : problems.filter(p => p.difficulty?.toLowerCase() === category);
    const categoryProblemIds = categoryProblems.map(p => p.id.toString());
    const allSelected = categoryProblemIds.every(id => selectedProblems.includes(id));
    
    if (allSelected) {
      // Deselect all in category
      setSelectedProblems(prev => prev.filter(id => !categoryProblemIds.includes(id)));
    } else {
      // Select all in category
      setSelectedProblems(prev => [...new Set([...prev, ...categoryProblemIds])]);
    }
  };

  const handleStartSession = () => {
    if (selectedProblems.length === 0) {
      toast({
        title: "No Problems Selected",
        description: "Please select at least one problem to continue",
        variant: "destructive"
      });
      return;
    }

    const selectedProblemDetails = problems.filter(p => selectedProblems.includes(p.id.toString()));
    console.log('Starting session with problems:', selectedProblemDetails);

    toast({
      title: isContest ? "Contest Starting!" : "Room Starting!",
      description: `${selectedProblems.length} problems selected`,
    });

    if (isContest) {
      // For contests, navigate to create contest page
      navigate('/create-contest', {
        state: {
          selectedProblems: selectedProblemDetails.map(p => p.id)
        }
      });
    } else {
      // For general rooms, navigate to the coding room
      navigate(`/room/${roomCode}`, {
        state: {
          roomName: roomName || contestName,
          isContest,
          selectedProblems: selectedProblemDetails
        }
      });
    }
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty?.toUpperCase()) {
      case 'EASY': return 'bg-success/20 text-success border-success/30';
      case 'MEDIUM': return 'bg-warning/20 text-warning border-warning/30';
      case 'HARD': return 'bg-destructive/20 text-destructive border-destructive/30';
      default: return 'bg-secondary/20 text-secondary-foreground border-secondary/30';
    }
  };

  const getDifficultyDisplayName = (difficulty: string) => {
    return difficulty?.charAt(0).toUpperCase() + difficulty?.slice(1).toLowerCase() || 'Unknown';
  };

  const totalSelectedProblems = selectedProblems.length;

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
                {isContest ? <Trophy className="h-5 w-5 text-warning" /> : <Play className="h-5 w-5 text-primary" />}
              </div>
              <div>
                <h1 className="text-xl font-bold">
                  {isContest ? 'Select Contest Problems' : 'Select Room Problems'}
                </h1>
                <p className="text-sm text-muted-foreground">
                  Choose problems for {isContest ? 'your contest' : 'your coding session'}
                </p>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="container mx-auto px-4 py-8">
        <div className="max-w-6xl mx-auto">
          <ScrollArea className="h-[calc(100vh-200px)]">
            <div className="space-y-8 pr-4">
          
          {/* Topic Selection */}
          <Card className="bg-gradient-card border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                {isContest ? <Trophy className="h-5 w-5 text-warning" /> : <Play className="h-5 w-5 text-primary" />}
                Select Topic
              </CardTitle>
              <CardDescription>
                Choose a topic to browse available problems
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3">
                {topics.map(topic => (
                  <Button
                    key={topic.topicId}
                    variant={selectedTopic === topic.topicId ? "default" : "outline"}
                    onClick={() => setSelectedTopic(topic.topicId)}
                    className="h-auto p-4 flex flex-col items-center gap-2"
                  >
                    <span className="font-medium">{topic.topicName}</span>
                    <span className="text-xs text-muted-foreground">ID: {topic.topicId}</span>
                  </Button>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Problem Selection */}
          {selectedTopic && (
            <Card className="bg-gradient-card border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  {isContest ? <Trophy className="h-5 w-5 text-warning" /> : <Play className="h-5 w-5 text-primary" />}
                  Select Problems
                </CardTitle>
                <CardDescription>
                  Choose problems from {topics.find(t => t.topicId === selectedTopic)?.topicName}
                  {totalSelectedProblems > 0 && ` (${totalSelectedProblems} selected)`}
                </CardDescription>
              </CardHeader>
              <CardContent>
                {loading ? (
                  <div className="text-center py-8">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
                    <p className="mt-2 text-muted-foreground">Loading problems...</p>
                  </div>
                ) : problems.length === 0 ? (
                  <div className="text-center py-8">
                    <p className="text-muted-foreground">No problems found for this topic</p>
                  </div>
                ) : (
                  <>
                    {/* Category Filter */}
                    <div className="flex flex-wrap gap-2 mb-6">
                      {categories.map(category => (
                        <Button
                          key={category}
                          variant={activeCategory === category ? "default" : "outline"}
                          size="sm"
                          onClick={() => setActiveCategory(category)}
                        >
                          {category === 'all' ? 'All' : getDifficultyDisplayName(category)}
                          {category !== 'all' && (
                            <Badge variant="secondary" className="ml-2">
                              {problemsByDifficulty[category]?.length || 0}
                            </Badge>
                          )}
                        </Button>
                      ))}
                    </div>

                    {/* Problems Grid */}
                    <div className="grid gap-3">
                      {filteredProblems.map(problem => (
                        <div
                          key={problem.id}
                          className={`p-4 border rounded-lg cursor-pointer transition-colors ${
                            selectedProblems.includes(problem.id.toString())
                              ? 'border-primary bg-primary/10'
                              : 'border-border hover:border-primary/50'
                          }`}
                          onClick={() => handleProblemToggle(problem.id.toString())}
                        >
                          <div className="flex items-center justify-between">
                            <div>
                              <h3 className="font-medium">{problem.title}</h3>
                              <p className="text-sm text-muted-foreground">ID: {problem.id}</p>
                            </div>
                            <div className="flex items-center gap-2">
                              <Badge 
                                variant="outline" 
                                className={getDifficultyColor(problem.difficulty)}
                              >
                                {problem.difficulty}
                              </Badge>
                              {selectedProblems.includes(problem.id.toString()) && (
                                <Check className="h-4 w-4 text-primary" />
                              )}
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </>
                )}
              </CardContent>
            </Card>
          )}

            </div>
          </ScrollArea>
        </div>
      </div>

      {/* Sticky Start Session Button */}
      {totalSelectedProblems > 0 && (
        <div className="fixed bottom-0 left-0 right-0 bg-background border-t border-border p-4 z-50">
          <div className="container mx-auto max-w-6xl">
            <Button 
              onClick={handleStartSession}
              className="w-full bg-primary hover:bg-primary/90 text-primary-foreground"
              disabled={loading}
            >
              {loading ? 'Loading...' : `Start ${isContest ? 'Contest' : 'Session'} with ${totalSelectedProblems} Problem${totalSelectedProblems > 1 ? 's' : ''}`}
              <ArrowRight className="ml-2 h-4 w-4" />
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default SelectProblems;