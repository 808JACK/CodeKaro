import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft, ArrowRight, Code, Users } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';

const CreateRoom = () => {
  const [roomName, setRoomName] = useState('');
  const navigate = useNavigate();
  const { toast } = useToast();

  // Mock problem categories
  const categories = [
    { id: 'arrays', name: 'Arrays', count: 45, difficulty: 'Easy to Hard' },
    { id: 'strings', name: 'Strings', count: 38, difficulty: 'Easy to Hard' },
    { id: 'linked-list', name: 'Linked Lists', count: 25, difficulty: 'Medium to Hard' },
    { id: 'trees', name: 'Trees', count: 32, difficulty: 'Medium to Hard' },
    { id: 'graphs', name: 'Graphs', count: 28, difficulty: 'Hard' },
    { id: 'dynamic-programming', name: 'Dynamic Programming', count: 35, difficulty: 'Hard' },
    { id: 'sorting', name: 'Sorting & Searching', count: 22, difficulty: 'Easy to Medium' },
    { id: 'math', name: 'Mathematics', count: 18, difficulty: 'Easy to Hard' }
  ];

  const handleCreateRoom = () => {
    if (!roomName.trim()) {
      toast({
        title: "Room Name Required",
        description: "Please enter a name for your room",
        variant: "destructive"
      });
      return;
    }

    // Generate random room code
    const roomCode = Math.random().toString(36).substring(2, 10).toUpperCase();
    
    toast({
      title: "Room Created!",
      description: `Room code: ${roomCode}`,
    });

    // Navigate to problem selection
    navigate(`/select-problems/${roomCode}`, { 
      state: { roomName, isContest: false } 
    });
  };

  const handleCategorySelect = (categoryId: string) => {
    // Generate random room code for quick start
    const roomCode = Math.random().toString(36).substring(2, 10).toUpperCase();
    const selectedCategory = categories.find(cat => cat.id === categoryId);
    
    navigate(`/select-problems/${roomCode}`, { 
      state: { 
        roomName: roomName || `${selectedCategory?.name} Practice`,
        selectedCategory: categoryId,
        isContest: false
      } 
    });
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
              <div className="p-2 bg-primary/10 rounded-lg">
                <Users className="h-5 w-5 text-primary" />
              </div>
              <div>
                <h1 className="text-xl font-bold">Create Collaborative Room</h1>
                <p className="text-sm text-muted-foreground">Start coding together</p>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="container mx-auto px-4 py-8">
        <div className="max-w-4xl mx-auto space-y-8">
          
          {/* Room Setup */}
          <Card className="bg-gradient-card border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Code className="h-5 w-5 text-primary" />
                Room Configuration
              </CardTitle>
              <CardDescription>
                Set up your collaborative coding session
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="roomName">Room Name</Label>
                <Input
                  id="roomName"
                  placeholder="e.g., Algorithm Practice Session"
                  value={roomName}
                  onChange={(e) => setRoomName(e.target.value)}
                  className="text-lg"
                />
                <p className="text-sm text-muted-foreground">
                  Give your room a descriptive name so participants know what to expect
                </p>
              </div>

              <div className="flex gap-4">
                <Button 
                  onClick={handleCreateRoom}
                  className="flex-1"
                  disabled={!roomName.trim()}
                >
                  Create Room & Select Problems
                  <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Quick Start with Categories */}
          <div className="space-y-6">
            <div>
              <h2 className="text-2xl font-bold mb-2">Quick Start by Topic</h2>
              <p className="text-muted-foreground">
                Jump straight into a topic-focused session
              </p>
            </div>

            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
              {categories.map((category) => (
                <Card 
                  key={category.id}
                  className="bg-card/50 border-border/50 hover:bg-card/80 hover:shadow-glow transition-all duration-300 cursor-pointer group"
                  onClick={() => handleCategorySelect(category.id)}
                >
                  <CardContent className="p-6">
                    <div className="space-y-4">
                      <div className="flex items-start justify-between">
                        <h3 className="font-semibold text-lg group-hover:text-primary transition-colors">
                          {category.name}
                        </h3>
                        <ArrowRight className="h-4 w-4 text-muted-foreground group-hover:text-primary transition-colors" />
                      </div>
                      
                      <div className="space-y-2">
                        <div className="flex items-center justify-between text-sm">
                          <span className="text-muted-foreground">Problems available</span>
                          <Badge variant="secondary">{category.count}</Badge>
                        </div>
                        <div className="flex items-center justify-between text-sm">
                          <span className="text-muted-foreground">Difficulty range</span>
                          <span className="text-foreground font-medium">{category.difficulty}</span>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>

          {/* Info Section */}
          <Card className="bg-muted/20 border-border/50">
            <CardContent className="p-6">
              <h3 className="font-semibold mb-4 flex items-center gap-2">
                <Users className="h-5 w-5 text-primary" />
                Collaborative Features
              </h3>
              <div className="grid md:grid-cols-2 gap-6 text-sm">
                <div className="space-y-3">
                  <div>
                    <h4 className="font-medium text-foreground">Real-time Code Editing</h4>
                    <p className="text-muted-foreground">See teammates' cursors and edits live</p>
                  </div>
                  <div>
                    <h4 className="font-medium text-foreground">Integrated Chat</h4>
                    <p className="text-muted-foreground">Text and voice communication</p>
                  </div>
                </div>
                <div className="space-y-3">
                  <div>
                    <h4 className="font-medium text-foreground">Problem Sharing</h4>
                    <p className="text-muted-foreground">Everyone sees the same questions</p>
                  </div>
                  <div>
                    <h4 className="font-medium text-foreground">Session Persistence</h4>
                    <p className="text-muted-foreground">Resume where you left off</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

        </div>
      </div>
    </div>
  );
};

export default CreateRoom;