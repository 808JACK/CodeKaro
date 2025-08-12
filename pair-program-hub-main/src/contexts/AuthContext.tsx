import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthService } from '@/services/authService';

interface User {
  id: string;
  email: string;
  username: string;
  displayName: string;
  avatar?: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => boolean;
  logout: () => void;
  signup: (email: string, password: string, username: string) => boolean;
  updateProfile: (updates: Partial<User>) => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Hardcoded credentials for dummy auth
const VALID_CREDENTIALS = {
  email: 'admin@codecollab.dev',
  password: 'password123'
};

const DUMMY_USER: User = {
  id: 'user_123',
  email: 'admin@codecollab.dev',
  username: 'admin',
  displayName: 'Admin User',
  avatar: undefined
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    // Check if user is authenticated using AuthService
    const isAuth = AuthService.isAuthenticated();
    if (isAuth) {
      const storedUserInfo = AuthService.getStoredUserInfo();
      const storedProfile = AuthService.getStoredUserProfile();
      
      if (storedUserInfo) {
        setUser({
          id: storedUserInfo.userId.toString(),
          email: storedUserInfo.email,
          username: storedUserInfo.username,
          displayName: storedProfile?.displayName || storedUserInfo.username,
          avatar: storedProfile?.avatarUrl
        });
      }
    }
  }, []);

  // Update authentication state when localStorage changes
  useEffect(() => {
    const handleStorageChange = () => {
      const isAuth = AuthService.isAuthenticated();
      if (!isAuth) {
        setUser(null);
      } else {
        const storedUserInfo = AuthService.getStoredUserInfo();
        const storedProfile = AuthService.getStoredUserProfile();
        
        if (storedUserInfo) {
          setUser({
            id: storedUserInfo.userId.toString(),
            email: storedUserInfo.email,
            username: storedUserInfo.username,
            displayName: storedProfile?.displayName || storedUserInfo.username,
            avatar: storedProfile?.avatarUrl
          });
        }
      }
    };

    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, []);

  const login = (email: string, password: string): boolean => {
    // This is now handled by the Login component directly
    // This function is kept for backward compatibility
    return false;
  };

  const signup = (email: string, password: string, username: string): boolean => {
    // This is now handled by the Signup component directly
    // This function is kept for backward compatibility
    return false;
  };

  const logout = () => {
    AuthService.logout();
    setUser(null);
    // Force redirect to landing page
    window.location.href = '/';
  };

  const updateProfile = (updates: Partial<User>) => {
    if (user) {
      const updatedUser = { ...user, ...updates };
      setUser(updatedUser);
      // TODO: Implement profile update API call
    }
  };

  const isAuthenticated = AuthService.isAuthenticated();

  return (
    <AuthContext.Provider value={{
      user,
      login,
      logout,
      signup,
      updateProfile,
      isAuthenticated
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};