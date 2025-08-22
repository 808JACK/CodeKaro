import { API_CONFIG, STORAGE_KEYS } from '../config/api';
import {fetchWithAuth} from './apiClient'

export interface RecentRoom {
  id: string; // Contest code (e.g., "B0Q95PCG")
  name: string; // Contest name
  type: string; // "contest"
  participants: number; // Total participants
  lastActive: string; // User's last activity time
  createdAt: string; // Contest start time
}

export interface ContestRoomData {
  contestId: number;
  contestCode: string;
  contestName: string;
  description: string;
  startTime: string;
  endTime: string;
  durationMinutes: number;
  problemIds: number[];
  userSubmissions: ContestSubmission[];
  totalUserSubmissions: number;
  userAcceptedSubmissions: number;
  userLastActivity: string;
  totalParticipants: number;
}

export interface ContestSubmission {
  id: string;
  submissionNumber: number;
  contestId: number;
  userId: number;
  userName: string;
  problemId: number;
  problemTitle: string;
  code: string;
  language: string;
  isAccepted: boolean;
  score: number;
  executionTimeMs: number;
  memoryUsedKb: number;
  status: string;
  errorMessage: string;
  testCasesPassed: number;
  totalTestCases: number;
  submittedAt: string;
  judgedAt: string;
  timeFromStartMs: number;
}

class DashboardService {
  private getAuthHeaders() {
    const userId = localStorage.getItem(STORAGE_KEYS.USER_ID);
    const token = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
    
    return {
      'Content-Type': 'application/json',
      'X-User-Id': userId || '',
      'Authorization': token ? `Bearer ${token}` : ''
    };
  }

  /**
   * Get user's top 5 recent contest rooms for dashboard
   * Called when user visits http://localhost:4000/ after login
   */
  async getRecentRooms(): Promise<RecentRoom[]> {
    try {
      console.log('🏠 Dashboard: Fetching recent contest rooms...');

      const recentRooms: RecentRoom[] = await fetchWithAuth(
        `${API_CONFIG.BASE_URL}/collab/api/contests/recent-rooms`,
        { headers: this.getAuthHeaders() }
      );

      console.log('✅ Dashboard: Loaded', recentRooms.length, 'recent rooms');
      return recentRooms;
    } catch (error) {
      console.error('❌ Dashboard: Error fetching recent rooms:', error);
      throw error;
    }
  }


  /**
   * Get detailed contest room data when user clicks on a room
   * Loads contest metadata + user's submissions from MongoDB
   */
    async getContestRoomData(contestCode: string): Promise<ContestRoomData> { 
    try {
      console.log('🏠 Dashboard: Loading room data for contest:', contestCode);
      
      const url = API_CONFIG.CONTEST.GET_ROOM_DASHBOARD_DATA.replace('{contestCode}', contestCode);
      
      const roomData: ContestRoomData = await fetchWithAuth(url, { method: 'GET' });

      console.log(
        '✅ Dashboard: Loaded room data for',
        contestCode,
        '- Submissions:',
        roomData.totalUserSubmissions
      );
      
      return roomData;
    } catch (error) {
      console.error('❌ Dashboard: Error fetching room data:', error);
      throw error;
    }
  }

  /**
   * Get user's submissions for a specific contest
   * Used when viewing contest submission history
   */
    async getUserContestSubmissions(contestCode: string): Promise<ContestSubmission[]> {
    try {
      console.log('📊 Loading submissions for contest:', contestCode);

      const url = API_CONFIG.CONTEST.GET_USER_SUBMISSIONS.replace('{contestCode}', contestCode);
      
      const submissions: ContestSubmission[] = await fetchWithAuth(url, { method: 'GET' });

      console.log('✅ Loaded', submissions.length, 'submissions for contest', contestCode);

      return submissions;
    } catch (error) {
      console.error('❌ Error fetching contest submissions:', error);
      throw error;
    }
  }

  /**
   * Format time ago string for better display
   */
  formatTimeAgo(timeString: string): string {
    if (!timeString || timeString === 'No activity') return 'No activity';
    
    try {
      const time = new Date(timeString);
      const now = new Date();
      const diffMs = now.getTime() - time.getTime();
      
      const minutes = Math.floor(diffMs / (1000 * 60));
      const hours = Math.floor(diffMs / (1000 * 60 * 60));
      const days = Math.floor(diffMs / (1000 * 60 * 60 * 24));
      
      if (days > 0) return `${days} day${days > 1 ? 's' : ''} ago`;
      if (hours > 0) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
      if (minutes > 0) return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
      return 'Just now';
    } catch (error) {
      return timeString; // Return original if parsing fails
    }
  }

  /**
   * Get contest status based on start/end times
   */
  getContestStatus(startTime: string, endTime: string): 'upcoming' | 'active' | 'ended' {
    const now = new Date();
    const start = new Date(startTime);
    const end = new Date(endTime);
    
    if (now < start) return 'upcoming';
    if (now > end) return 'ended';
    return 'active';
  }

  /**
   * Calculate contest progress percentage
   */
  getContestProgress(startTime: string, endTime: string): number {
    const now = new Date();
    const start = new Date(startTime);
    const end = new Date(endTime);
    
    if (now < start) return 0;
    if (now > end) return 100;
    
    const total = end.getTime() - start.getTime();
    const elapsed = now.getTime() - start.getTime();
    
    return Math.round((elapsed / total) * 100);
  }
}

export const dashboardService = new DashboardService();