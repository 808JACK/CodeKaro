import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { User, Edit, Save, X, Calendar, MapPin } from 'lucide-react';
import { useToast } from '@/hooks/use-toast';
import { AuthService } from '@/services/authService';

const Profile = () => {
  const { toast } = useToast();
  const [isEditing, setIsEditing] = useState(false);
  const [userInfo, setUserInfo] = useState<any>(null);
  const [userProfile, setUserProfile] = useState<any>(null);
  const [formData, setFormData] = useState({
    username: '',
    displayName: '',
    email: '',
    bio: '',
    customStatus: ''
  });

  // Load user data on component mount
  useEffect(() => {
    const loadUserData = () => {
      const storedUserInfo = AuthService.getStoredUserInfo();
      const storedProfile = AuthService.getStoredUserProfile();
      
      if (storedUserInfo) {
        setUserInfo(storedUserInfo);
        setFormData(prev => ({
          ...prev,
          username: storedUserInfo.username || '',
          email: storedUserInfo.email || ''
        }));
      }
      
      if (storedProfile) {
        setUserProfile(storedProfile);
        setFormData(prev => ({
          ...prev,
          displayName: storedProfile.displayName || '',
          bio: storedProfile.bio || '',
          customStatus: storedProfile.customStatus || ''
        }));
      }
    };

    loadUserData();
  }, []);

  const handleSave = () => {
    // TODO: Implement profile update API call
    setIsEditing(false);
    toast({
      title: "Profile updated",
      description: "Your profile has been updated successfully."
    });
  };

  const handleCancel = () => {
    // Reset form data to current values
    if (userInfo) {
      setFormData(prev => ({
        ...prev,
        username: userInfo.username || '',
        email: userInfo.email || ''
      }));
    }
    if (userProfile) {
      setFormData(prev => ({
        ...prev,
        displayName: userProfile.displayName || '',
        bio: userProfile.bio || '',
        customStatus: userProfile.customStatus || ''
      }));
    }
    setIsEditing(false);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleLogout = () => {
    AuthService.logout();
    toast({
      title: "Logged out",
      description: "You have been logged out successfully."
    });
  };

  if (!userInfo && !userProfile) return null;

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="ghost" size="sm" className="flex items-center gap-2">
          <Avatar className="h-6 w-6">
            {userProfile?.avatarUrl ? (
              <AvatarImage src={`http://localhost:8086${userProfile.avatarUrl}`} alt={userProfile.displayName} />
            ) : (
              <AvatarFallback className="text-xs bg-primary text-primary-foreground">
                {(userProfile?.displayName || userInfo?.username || 'U').charAt(0).toUpperCase()}
              </AvatarFallback>
            )}
          </Avatar>
          <span className="hidden md:inline">{userProfile?.displayName || userInfo?.username || 'User'}</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <User className="h-5 w-5" />
            Profile Settings
          </DialogTitle>
          <DialogDescription>
            Manage your account information and preferences.
          </DialogDescription>
        </DialogHeader>
        
        <Card>
          <CardHeader className="text-center pb-2">
            <Avatar className="h-20 w-20 mx-auto mb-4">
              {userProfile?.avatarUrl ? (
                <AvatarImage src={`http://localhost:8086${userProfile.avatarUrl}`} alt={userProfile.displayName} />
              ) : (
                <AvatarFallback className="text-2xl bg-gradient-primary text-primary-foreground">
                  {(userProfile?.displayName || userInfo?.username || 'U').charAt(0).toUpperCase()}
                </AvatarFallback>
              )}
            </Avatar>
            <CardTitle className="text-xl">{userProfile?.displayName || userInfo?.username || 'User'}</CardTitle>
            <CardDescription>@{userInfo?.username || 'username'}</CardDescription>
          </CardHeader>
          
          <CardContent className="space-y-4">
            {isEditing ? (
              <div className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="username">Username</Label>
                  <Input
                    id="username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    placeholder="Username"
                  />
                </div>
                
                <div className="space-y-2">
                  <Label htmlFor="displayName">Display Name</Label>
                  <Input
                    id="displayName"
                    name="displayName"
                    value={formData.displayName}
                    onChange={handleChange}
                    placeholder="Display Name"
                  />
                </div>
                
                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Email"
                  />
                </div>
                
                <div className="flex gap-2">
                  <Button onClick={handleSave} className="flex-1">
                    <Save className="h-4 w-4 mr-2" />
                    Save
                  </Button>
                  <Button onClick={handleCancel} variant="outline" className="flex-1">
                    <X className="h-4 w-4 mr-2" />
                    Cancel
                  </Button>
                </div>
              </div>
            ) : (
              <div className="space-y-4">
                <div className="space-y-4">
                  {/* Display Name and Username */}
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <div className="text-muted-foreground">Display Name</div>
                      <div className="font-medium">{userProfile?.displayName || 'Not set'}</div>
                    </div>
                    <div>
                      <div className="text-muted-foreground">Username</div>
                      <div className="font-medium">@{userInfo?.username || 'username'}</div>
                    </div>
                  </div>
                  
                  {/* Email */}
                  <div className="text-sm">
                    <div className="text-muted-foreground">Email</div>
                    <div className="font-medium">{userInfo?.email || 'Not set'}</div>
                  </div>
                  
                  {/* Bio */}
                  {userProfile?.bio && (
                    <div className="text-sm">
                      <div className="text-muted-foreground">Bio</div>
                      <div className="font-medium">{userProfile.bio}</div>
                    </div>
                  )}
                  
                  {/* Custom Status */}
                  {userProfile?.customStatus && (
                    <div className="text-sm">
                      <div className="text-muted-foreground">Status</div>
                      <div className="font-medium">{userProfile.customStatus}</div>
                    </div>
                  )}
                  
                  {/* Joined Date */}
                  {userProfile?.createdAt && (
                    <div className="text-sm">
                      <div className="text-muted-foreground">Joined</div>
                      <div className="font-medium flex items-center gap-1">
                        <Calendar className="h-3 w-3" />
                        {new Date(userProfile.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                  )}
                </div>
                
                <div className="flex gap-2 pt-4">
                  <Button 
                    onClick={() => setIsEditing(true)} 
                    variant="outline" 
                    className="flex-1"
                  >
                    <Edit className="h-4 w-4 mr-2" />
                    Edit Profile
                  </Button>
                  <Button 
                    onClick={handleLogout} 
                    variant="destructive" 
                    className="flex-1"
                  >
                    Logout
                  </Button>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </DialogContent>
    </Dialog>
  );
};

export default Profile;