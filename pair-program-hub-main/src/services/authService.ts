import { API_CONFIG, STORAGE_KEYS } from '../config/api';
import { fetchWithAuth } from './apiClient';

// Types
export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  data: {
    userId: number;
    username: string;
    displayName: string | null;
    email: string;
    accessToken: string;
    refreshToken: string;
  };
}

export interface UserProfile {
  id: number;
  userId: number;
  displayName: string;
  avatarUrl: string;
  bio: string;
  customStatus: string;
  createdAt: number;
  updatedAt: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

// Authentication Service
export class AuthService {
  // Signup - Step 1: Send OTP
  static async signup(signupData: SignupRequest): Promise<ApiResponse<string>> {
    try {
      const response = await fetch(API_CONFIG.AUTH.SIGNUP, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(signupData),
      });

      const result = await response.json();
      
      if (!response.ok) {
        throw new Error(result.message || 'Signup failed');
      }

      return result;
    } catch (error) {
      console.error('Signup error:', error);
      throw error;
    }
  }

  // Verify OTP and Save User - Step 2: Complete signup
  static async verifyOtpAndSaveUser(
    email: string, 
    otp: string, 
    signupData: SignupRequest
  ): Promise<ApiResponse<string>> {
    try {
      const url = `${API_CONFIG.AUTH.VERIFY_OTP}?email=${encodeURIComponent(email)}&otp=${encodeURIComponent(otp)}`;
      
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(signupData),
      });

      const result = await response.json();
      
      if (!response.ok) {
        throw new Error(result.message || 'OTP verification failed');
      }

      return result;
    } catch (error) {
      console.error('OTP verification error:', error);
      throw error;
    }
  }

  // Login
  static async login(loginData: LoginRequest): Promise<LoginResponse> {
    try {
      const response = await fetch(API_CONFIG.AUTH.LOGIN, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
      });

      const result = await response.json();
      
      if (!response.ok) {
        throw new Error(result.message || 'Login failed');
      }

      // Save tokens and user info to localStorage
      if (result.success && result.data) {
        localStorage.setItem(STORAGE_KEYS.USER_ID, result.data.userId.toString());
        localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, result.data.accessToken);
        localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, result.data.refreshToken);
        localStorage.setItem(STORAGE_KEYS.USER_INFO, JSON.stringify({
          userId: result.data.userId,
          username: result.data.username,
          email: result.data.email
        }));
      }

      return result;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  }

  // Get User Profile
    static async getUserProfile(userId: number): Promise<ApiResponse<UserProfile>> {
    try {
      const result = await fetchWithAuth(`${API_CONFIG.PROFILE.GET_PROFILE}/${userId}`);

      // Save profile to localStorage
      if (result.success && result.data) {
        localStorage.setItem(STORAGE_KEYS.USER_PROFILE, JSON.stringify(result.data));
      }

      return result;
    } catch (error) {
      console.error('Get profile error:', error);
      throw error;
    }
  }

  // Get User Info from Token
  static async getUserInfo(): Promise<any> {
    try {
      const token = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
      
      if (!token) {
        throw new Error('No access token found');
      }

      const response = await fetch(API_CONFIG.TOKEN.USER_INFO, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to get user info');
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error('Get user info error:', error);
      throw error;
    }
  }

  // Logout
  static logout(): void {
    localStorage.removeItem(STORAGE_KEYS.USER_ID);
    localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.USER_PROFILE);
    localStorage.removeItem(STORAGE_KEYS.USER_INFO);
  }

  // Check if user is authenticated
  static isAuthenticated(): boolean {
    const token = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
    const userId = localStorage.getItem(STORAGE_KEYS.USER_ID);
    return !!(token && userId);
  }

  // Get stored user info
  static getStoredUserInfo(): any {
    const userInfo = localStorage.getItem(STORAGE_KEYS.USER_INFO);
    return userInfo ? JSON.parse(userInfo) : null;
  }

  // Get stored user profile
  static getStoredUserProfile(): UserProfile | null {
    const profile = localStorage.getItem(STORAGE_KEYS.USER_PROFILE);
    return profile ? JSON.parse(profile) : null;
  }
}
