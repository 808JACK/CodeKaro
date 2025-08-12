import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Code, Users, Trophy, ArrowRight, Clock, Gamepad2, LogOut } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { AuthService } from '@/services/authService';
import Profile from '@/components/Profile';

const Dashboard = () => {
  const [roomCode, setRoomCode] = useState('');
  const [userInfo, setUserInfo] = useState<any>(null);
  const [userProfile, setUserProfile] = useState<any>(null);
  const navigate = useNavigate();
  const { toast } = useToast();

  // Load user data on component mount
  useEffect(() => {
    const loadUserData = () => {
      const storedUserInfo = AuthService.getStoredUserInfo();
      const storedProfile = AuthService.getStoredUserProfile();
      
      if (storedUserInfo) {
        setUserInfo(storedUserInfo);
      }
      
      if (storedProfile) {
        setUserProfile(storedProfile);
      }
    };

    loadUserData();
  }, []);

  const handleLogout = () => {
    AuthService.logout();
    toast({
      title: "Logged out",
      description: "You have been logged out successfully."
    });
    // Force a page refresh to update the AuthContext and redirect to landing
    window.location.href = '/';
  };

  // Mock recent rooms data
  const recentRooms = [
    { id: 'ABC12345', name: 'Algorithm Practice', participants: 3, lastActive: '2 hours ago', type: 'collaborative' },
    { id: 'DEF67890', name: 'Contest Battle', participants: 8, lastActive: '1 day ago', type: 'contest' },
    { id: 'GHI45612', name: 'Data Structures', participants: 2, lastActive: '3 days ago', type: 'collaborative' }
  ];

  // Mock upcoming contests
  const upcomingContests = [
    { 
      id: 'CON001', 
      name: 'Weekly Algorithm Challenge', 
      startTime: 'In 2 hours', 
      duration: '90 minutes',
      participants: 15,
      difficulty: 'Medium'
    },
    { 
      id: 'CON002', 
      name: 'Data Structure Sprint', 
      startTime: 'Tomorrow 9:00 AM', 
      duration: '60 minutes',
      participants: 8,
      difficulty: 'Easy'
    },
    { 
      id: 'CON003', 
      name: 'Expert Championship', 
      startTime: 'Saturday 2:00 PM', 
      duration: '3 hours',
      participants: 25,
      difficulty: 'Hard'
    }
  ];

  const [contestCode, setContestCode] = useState('');

  const handleJoinRoom = () => {
    if (roomCode.length !== 8) {
      toast({
        title: "Invalid Room Code",
        description: "Room code must be 8 characters long",
        variant: "destructive"
      });
      return;
    }
    
    // Simulate API call
    toast({
      title: "Joining Room...",
      description: `Connecting to room ${roomCode}`
    });
    
    setTimeout(() => {
      navigate(`/room/${roomCode}`);
    }, 1000);
  };

  const handleJoinContest = () => {
    if (!contestCode.trim()) {
      toast({
        title: "Invalid Contest Code",
        description: "Please enter a contest code",
        variant: "destructive"
      });
      return;
    }
    
    console.log('=== DASHBOARD: JOINING CONTEST ===');
    console.log('Contest code:', contestCode);
    console.log('Navigating to:', `/contest/${contestCode}`);
    
    // Navigate directly to contest room
    navigate(`/contest/${contestCode}`);
  };

  const handleCreateRoom = () => {
    navigate('/create-room');
  };

  const handleCreateContest = () => {
    navigate('/create-contest');
  };

  const handleJoinRecentRoom = (roomId: string) => {
    navigate(`/room/${roomId}`);
  };

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-gradient-primary rounded-lg">
                <Code className="h-6 w-6 text-primary-foreground" />
              </div>
              <div>
                <h1 className="text-2xl font-bold bg-gradient-primary bg-clip-text text-transparent">
                  CodeCollab
                </h1>
                <p className="text-sm text-muted-foreground">
                  Welcome back, {userProfile?.displayName || userInfo?.username || 'User'}!
                </p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <Profile />
              <Button 
                variant="ghost" 
                size="sm"
                onClick={handleLogout}
                className="text-muted-foreground hover:text-destructive"
              >
                <LogOut className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </header>

      <div className="container mx-auto px-4 py-12">
        <div className="max-w-6xl mx-auto space-y-12">
          
          {/* Hero Section */}
          <div className="text-center space-y-6">
            <h2 className="text-4xl md:text-6xl font-bold">
              Code <span className="bg-gradient-primary bg-clip-text text-transparent">Together</span>
            </h2>
            <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
              Real-time collaborative coding with friends. Practice algorithms, compete in contests, 
              and grow your skills together.
            </p>
          </div>

          {/* Main Actions */}
          <div className="grid md:grid-cols-3 gap-6">
            
            {/* Join Room */}
            <Card className="bg-gradient-card border-border/50 hover:shadow-glow transition-all duration-300">
              <CardHeader className="text-center">
                <div className="mx-auto p-3 bg-primary/10 rounded-full w-fit mb-4">
                  <Users className="h-8 w-8 text-primary" />
                </div>
                <CardTitle className="text-xl">Join Room</CardTitle>
                <CardDescription>
                  Enter a room code to join an existing collaboration session
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <Input
                  placeholder="Enter 8-character room code"
                  value={roomCode}
                  onChange={(e) => setRoomCode(e.target.value.toUpperCase())}
                  maxLength={8}
                  className="text-center text-lg font-mono tracking-widest"
                />
                <Button 
                  onClick={handleJoinRoom}
                  className="w-full"
                  disabled={roomCode.length !== 8}
                >
                  Join Room <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </CardContent>
            </Card>

            {/* Create Room */}
            <Card className="bg-gradient-card border-border/50 hover:shadow-glow transition-all duration-300">
              <CardHeader className="text-center">
                <div className="mx-auto p-3 bg-accent/10 rounded-full w-fit mb-4">
                  <Code className="h-8 w-8 text-accent" />
                </div>
                <CardTitle className="text-xl">Create Room</CardTitle>
                <CardDescription>
                  Start a new collaborative coding session with problem selection
                </CardDescription>
              </CardHeader>
              <CardContent>
                <Button 
                  onClick={handleCreateRoom}
                  variant="secondary" 
                  className="w-full"
                >
                  Create Room <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </CardContent>
            </Card>

            {/* Join Contest */}
            <Card className="bg-gradient-card border-border/50 hover:shadow-glow transition-all duration-300">
              <CardHeader className="text-center">
                <div className="mx-auto p-3 bg-warning/10 rounded-full w-fit mb-4">
                  <Trophy className="h-8 w-8 text-warning" />
                </div>
                <CardTitle className="text-xl">Join Contest</CardTitle>
                <CardDescription>
                  Enter a contest using the invite code
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <Input
                  placeholder="Enter contest code (e.g., 3IVQPDO8)"
                  value={contestCode}
                  onChange={(e) => setContestCode(e.target.value.toUpperCase())}
                  className="text-center text-lg font-mono tracking-widest"
                />
                <Button 
                  onClick={handleJoinContest}
                  className="w-full"
                  disabled={!contestCode.trim()}
                >
                  Join Contest <Trophy className="ml-2 h-4 w-4" />
                </Button>
              </CardContent>
            </Card>

            {/* Create Contest */}
            <Card className="bg-gradient-card border-border/50 hover:shadow-glow transition-all duration-300">
              <CardHeader className="text-center">
                <div className="mx-auto p-3 bg-warning/10 rounded-full w-fit mb-4">
                  <Trophy className="h-8 w-8 text-warning" />
                </div>
                <CardTitle className="text-xl">Create Contest</CardTitle>
                <CardDescription>
                  Host a timed coding competition with leaderboard tracking
                </CardDescription>
              </CardHeader>
              <CardContent>
                <Button 
                  onClick={handleCreateContest}
                  variant="outline" 
                  className="w-full border-warning/30 hover:bg-warning/10"
                >
                  Create Contest <Trophy className="ml-2 h-4 w-4" />
                </Button>
              </CardContent>
            </Card>
          </div>

          {/* Upcoming Contests */}
          {upcomingContests.length > 0 && (
            <div className="space-y-6">
              <h3 className="text-2xl font-semibold flex items-center gap-2">
                <Trophy className="h-6 w-6 text-warning" />
                Upcoming Contests
              </h3>
              
              <div className="grid gap-4">
                {upcomingContests.map((contest) => (
                  <Card 
                    key={contest.id}
                    className="bg-gradient-card border-border/50 hover:bg-card/80 transition-all duration-200 cursor-pointer"
                    onClick={() => handleJoinRecentRoom(contest.id)}
                  >
                    <CardContent className="p-4">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-4">
                          <div className="p-2 bg-warning/10 rounded-lg">
                            <Trophy className="h-5 w-5 text-warning" />
                          </div>
                          <div>
                            <h4 className="font-semibold">{contest.name}</h4>
                            <p className="text-sm text-muted-foreground">
                              {contest.startTime} • {contest.duration} • {contest.participants} registered
                            </p>
                          </div>
                        </div>
                        <div className="text-right">
                          <Badge variant="outline" className="border-warning/30 text-warning">
                            {contest.difficulty}
                          </Badge>
                          <p className="text-sm text-muted-foreground mt-1">{contest.startTime}</p>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </div>
          )}

          {/* Recent Rooms */}
          {recentRooms.length > 0 && (
            <div className="space-y-6">
              <h3 className="text-2xl font-semibold flex items-center gap-2">
                <Clock className="h-6 w-6 text-primary" />
                Recent Rooms
              </h3>
              
              <div className="grid gap-4">
                {recentRooms.map((room) => (
                  <Card 
                    key={room.id}
                    className="bg-card/50 border-border/50 hover:bg-card/80 transition-all duration-200 cursor-pointer"
                    onClick={() => handleJoinRecentRoom(room.id)}
                  >
                    <CardContent className="p-4">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-4">
                          <div className="p-2 bg-secondary rounded-lg">
                            {room.type === 'contest' ? (
                              <Gamepad2 className="h-5 w-5 text-warning" />
                            ) : (
                              <Users className="h-5 w-5 text-primary" />
                            )}
                          </div>
                          <div>
                            <h4 className="font-semibold">{room.name}</h4>
                            <p className="text-sm text-muted-foreground">
                              Code: {room.id} • {room.participants} participants
                            </p>
                          </div>
                        </div>
                        <div className="text-right">
                          <Badge variant={room.type === 'contest' ? 'destructive' : 'secondary'}>
                            {room.type === 'contest' ? 'Contest' : 'Collaborative'}
                          </Badge>
                          <p className="text-sm text-muted-foreground mt-1">{room.lastActive}</p>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </div>
          )}

          {/* Features */}
          <div className="grid md:grid-cols-3 gap-8 pt-12">
            <div className="text-center space-y-4">
              <div className="mx-auto p-4 bg-primary/10 rounded-full w-fit">
                <Code className="h-8 w-8 text-primary" />
              </div>
              <h3 className="text-xl font-semibold">Real-time Collaboration</h3>
              <p className="text-muted-foreground">
                See code changes instantly as your teammates type, just like Google Docs
              </p>
            </div>
            
            <div className="text-center space-y-4">
              <div className="mx-auto p-4 bg-accent/10 rounded-full w-fit">
                <Users className="h-8 w-8 text-accent" />
              </div>
              <h3 className="text-xl font-semibold">Integrated Chat</h3>
              <p className="text-muted-foreground">
                Communicate with text and voice messages while coding together
              </p>
            </div>
            
            <div className="text-center space-y-4">
              <div className="mx-auto p-4 bg-warning/10 rounded-full w-fit">
                <Trophy className="h-8 w-8 text-warning" />
              </div>
              <h3 className="text-xl font-semibold">Competitive Contests</h3>
              <p className="text-muted-foreground">
                Host timed competitions with live leaderboards and rankings
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;