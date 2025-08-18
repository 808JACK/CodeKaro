import { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Code, Users, Trophy, ArrowRight, LogOut, Clock, Gamepad2, RefreshCw } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { AuthService } from '@/services/authService';
import { ContestService, RecentRoom } from '@/services/contestService';
import { dashboardService } from '@/services/dashboardService';
import Profile from '@/components/Profile';

const Dashboard = () => {
  const [roomCode, setRoomCode] = useState('');
  const [userInfo, setUserInfo] = useState<unknown>(null);
  const [userProfile, setUserProfile] = useState<unknown>(null);
  const [recentRooms, setRecentRooms] = useState<RecentRoom[]>([]);
  const [loadingRecentRooms, setLoadingRecentRooms] = useState(false);
  const [lastLoadTime, setLastLoadTime] = useState<number>(0);
  const hasLoadedOnce = useRef(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { toast } = useToast();

  // Cache duration: 5 minutes (don't reload unless user returns from contest/room)
  const CACHE_DURATION = 5 * 60 * 1000;

  // Load user data and recent rooms with smart caching
  useEffect(() => {
    console.log('🏠 Dashboard: Component mounted/updated');
    console.log('🏠 Dashboard: Location state:', location.state);
    console.log('🏠 Dashboard: Has loaded once:', hasLoadedOnce.current);
    console.log('🏠 Dashboard: Last load time:', new Date(lastLoadTime).toISOString());

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

    const shouldLoadRecentRooms = () => {
      const now = Date.now();
      const timeSinceLastLoad = now - lastLoadTime;
      
      // Load if:
      // 1. Never loaded before
      // 2. Coming back from contest/room (location.state?.fromContest or fromRoom)
      // 3. Cache expired (older than 5 minutes)
      // 4. User explicitly refreshed
      
      const neverLoaded = !hasLoadedOnce.current;
      const returningFromContest = location.state?.fromContest === true;
      const returningFromRoom = location.state?.fromRoom === true;
      const cacheExpired = timeSinceLastLoad > CACHE_DURATION;
      const forceRefresh = location.state?.forceRefresh === true;
      
      console.log('🏠 Dashboard: Should load recent rooms?', {
        neverLoaded,
        returningFromContest,
        returningFromRoom,
        cacheExpired,
        forceRefresh,
        timeSinceLastLoad: Math.round(timeSinceLastLoad / 1000) + 's'
      });
      
      return neverLoaded || returningFromContest || returningFromRoom || cacheExpired || forceRefresh;
    };

    const loadRecentRooms = async () => {
      if (!shouldLoadRecentRooms()) {
        console.log('🏠 Dashboard: Skipping recent rooms load (using cache)');
        return;
      }

      try {
        console.log('🏠 Dashboard: Loading recent contest rooms...');
        setLoadingRecentRooms(true);
        
        // Use the enhanced dashboard service for better performance
        const rooms = await dashboardService.getRecentRooms();
        
        console.log('🏠 Dashboard: Loaded', rooms.length, 'recent rooms');
        setRecentRooms(rooms);
        setLastLoadTime(Date.now());
        hasLoadedOnce.current = true;
        
        // Clear location state to prevent unnecessary reloads
        if (location.state) {
          window.history.replaceState({}, document.title);
        }
        
      } catch (error) {
        console.error('🏠 Dashboard: Failed to load recent rooms:', error);
        // Don't show error toast for recent rooms as it's not critical
        // But log it for debugging
      } finally {
        setLoadingRecentRooms(false);
      }
    };

    loadUserData();
    loadRecentRooms();
  }, [location.state, lastLoadTime, CACHE_DURATION]);

  const handleLogout = () => {
    AuthService.logout();
    toast({
      title: "Logged out",
      description: "You have been logged out successfully."
    });
    // Force a page refresh to update the AuthContext and redirect to landing
    window.location.href = '/';
  };





  const handleJoinRoomOrContest = () => {
    if (!roomCode.trim()) {
      toast({
        title: "Invalid Code",
        description: "Please enter a room or contest code",
        variant: "destructive"
      });
      return;
    }
    
    console.log('=== DASHBOARD: JOINING ROOM/CONTEST ===');
    console.log('Code entered:', roomCode);
    
    // First try to join as a contest (exact logic from handleJoinContest)
    console.log('Attempting to join as contest first...');
    console.log('Navigating to:', `/contest/${roomCode}`);
    
    // Navigate directly to contest room - the ContestRoom component will handle
    // the logic to determine if it's a valid contest or fall back to room
    navigate(`/contest/${roomCode}`);
  };

  const handleCreateRoom = () => {
    navigate('/create-room');
  };

  const handleCreateContest = () => {
    navigate('/create-contest');
  };

  const handleJoinRecentRoom = (roomId: string, roomName: string) => {
    console.log('🏠 Dashboard: Joining recent room:', { roomId, roomName });
    
    // Use the same logic as the unified join - try contest first, then fallback to room
    navigate(`/contest/${roomId}`, {
      state: { fromDashboard: true }
    });
  };

  const handleRefreshRecentRooms = async () => {
    console.log('🏠 Dashboard: Manual refresh requested');
    setLastLoadTime(0); // Force cache invalidation
    hasLoadedOnce.current = false; // Force reload
    
    try {
      setLoadingRecentRooms(true);
      const rooms = await dashboardService.getRecentRooms();
      setRecentRooms(rooms);
      setLastLoadTime(Date.now());
      hasLoadedOnce.current = true;
      
      toast({
        title: "Refreshed",
        description: `Loaded ${rooms.length} recent contest rooms`,
      });
    } catch (error) {
      console.error('🏠 Dashboard: Manual refresh failed:', error);
      toast({
        title: "Refresh Failed",
        description: "Could not load recent rooms. Please try again.",
        variant: "destructive"
      });
    } finally {
      setLoadingRecentRooms(false);
    }
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
            
            {/* Join Room/Contest */}
            <Card className="bg-gradient-card border-border/50 hover:shadow-glow transition-all duration-300">
              <CardHeader className="text-center">
                <div className="mx-auto p-3 bg-primary/10 rounded-full w-fit mb-4">
                  <Users className="h-8 w-8 text-primary" />
                </div>
                <CardTitle className="text-xl">Join Room/Contest</CardTitle>
                <CardDescription>
                  Enter a room code or contest code to join an existing session
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <Input
                  placeholder="Enter room or contest code"
                  value={roomCode}
                  onChange={(e) => setRoomCode(e.target.value.toUpperCase())}
                  className="text-center text-lg font-mono tracking-widest"
                />
                <Button 
                  onClick={handleJoinRoomOrContest}
                  className="w-full"
                  disabled={!roomCode.trim()}
                >
                  Join Room/Contest <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </CardContent>
            </Card>

            {/* Create Room */}
            <Card className="bg-gradient-card border-border/50 opacity-60">
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
                  variant="secondary" 
                  className="w-full"
                  disabled
                >
                  Coming Soon
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

          {/* Recent Contest Rooms */}
          {(recentRooms.length > 0 || loadingRecentRooms || hasLoadedOnce.current) && (
            <div className="space-y-6">
              <div className="flex items-center justify-between">
                <h3 className="text-2xl font-semibold flex items-center gap-2">
                  <Clock className="h-6 w-6 text-primary" />
                  Recent Contest Rooms
                  <Badge variant="outline" className="ml-2">
                    Top 5
                  </Badge>
                </h3>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={handleRefreshRecentRooms}
                  disabled={loadingRecentRooms}
                  className="gap-2"
                >
                  <RefreshCw className={`h-4 w-4 ${loadingRecentRooms ? 'animate-spin' : ''}`} />
                  Refresh
                </Button>
              </div>
              
              {loadingRecentRooms ? (
                <div className="flex items-center justify-center py-8">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
                  <span className="ml-2 text-muted-foreground">Loading recent contest rooms...</span>
                </div>
              ) : recentRooms.length === 0 ? (
                <Card className="bg-card/30 border-dashed border-border/50">
                  <CardContent className="p-8 text-center">
                    <Trophy className="h-12 w-12 text-muted-foreground/50 mx-auto mb-4" />
                    <h4 className="text-lg font-semibold text-muted-foreground mb-2">
                      No Recent Contest Rooms
                    </h4>
                    <p className="text-sm text-muted-foreground mb-4">
                      You haven't participated in any contests yet. Create or join a contest to get started!
                    </p>
                    <div className="flex gap-2 justify-center">
                      <Button onClick={handleCreateContest} size="sm">
                        Create Contest
                      </Button>
                      <Button variant="outline" size="sm" onClick={() => setRoomCode('')}>
                        Join Contest
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              ) : (
                <div className="grid gap-4">
                  {recentRooms.map((room, index) => (
                    <Card 
                      key={room.id}
                      className="bg-card/50 border-border/50 hover:bg-card/80 transition-all duration-200 cursor-pointer group"
                      onClick={() => handleJoinRecentRoom(room.id, room.name)}
                    >
                      <CardContent className="p-4">
                        <div className="flex items-center justify-between">
                          <div className="flex items-center gap-4">
                            <div className="relative">
                              <div className="p-2 bg-secondary rounded-lg group-hover:bg-primary/10 transition-colors">
                                {room.type === 'contest' ? (
                                  <Trophy className="h-5 w-5 text-warning group-hover:text-primary transition-colors" />
                                ) : (
                                  <Users className="h-5 w-5 text-primary" />
                                )}
                              </div>
                              {index === 0 && (
                                <Badge className="absolute -top-2 -right-2 text-xs px-1 py-0 bg-primary">
                                  Latest
                                </Badge>
                              )}
                            </div>
                            <div>
                              <h4 className="font-semibold group-hover:text-primary transition-colors">
                                {room.name}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                Code: <span className="font-mono font-semibold">{room.id}</span> • 
                                {room.participants} participant{room.participants !== 1 ? 's' : ''}
                              </p>
                            </div>
                          </div>
                          <div className="text-right">
                            <Badge variant={room.type === 'contest' ? 'default' : 'secondary'}>
                              {room.type === 'contest' ? 'Contest' : 'Room'}
                            </Badge>
                            <p className="text-sm text-muted-foreground mt-1">
                              {dashboardService.formatTimeAgo(room.lastActive)}
                            </p>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                  
                  {/* Cache info for debugging */}
                  {lastLoadTime > 0 && (
                    <p className="text-xs text-muted-foreground text-center">
                      Last updated: {new Date(lastLoadTime).toLocaleTimeString()}
                    </p>
                  )}
                </div>
              )}
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