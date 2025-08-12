import { API_CONFIG, STORAGE_KEYS } from '../config/api';
import { AuthService } from './authService';

// Types
export interface Topic {
  topicId: number;
  topicName: string;
}

export interface Problem {
  id: number;
  title: string;
  difficulty: string;
}

export interface ProblemDetails {
  id: number;
  title: string;
  difficulty: string;
  topicIds: number[];
  createdAt: string | null;
  content: string;
  constraints: string;
  inputFormat: string;
  outputFormat: string;
  examples: Array<{
    input: string;
    output: string;
  }>;
  template: string;
  functionName: string;
  methodSignature: string;
  supportedLanguages: string[];
}

export interface CreateContestRequest {
  title: string;
  description: string;
  startTime: string;
  duration: number; // in minutes
  problemIds: number[];
  maxParticipants: number;
}

export interface ContestDetails {
  id: string;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  duration: number;
  problemIds: number[];
  maxParticipants: number;
  currentParticipants: number;
  status: 'UPCOMING' | 'ONGOING' | 'COMPLETED';
  createdBy: number;
}

export interface CodeExecutionRequest {
  code: string;
  language: string;
  problemName: string;
  timeLimitMs: number;
  memoryLimitMb: number;
}

export interface TestResult {
  testNumber: number;
  verdict: string;
  timeMs: number;
  output: string;
  error: string;
}

export interface CodeExecutionResponse {
  overallVerdict: string;
  testResults: TestResult[];
  error?: string;
}

// Contest Service
export class ContestService {
  // Get all topics
  static async getTopics(): Promise<Topic[]> {
    try {
      const response = await fetch(API_CONFIG.PROBLEM.GET_TOPICS);
      const result = await response.json();
      
      if (!response.ok) {
        throw new Error(result.message || 'Failed to fetch topics');
      }

      return result;
    } catch (error) {
      console.error('Get topics error:', error);
      throw error;
    }
  }

  // Get problems by topic
  static async getProblemsByTopic(topicId: number): Promise<Problem[]> {
    try {
      const response = await fetch(`${API_CONFIG.PROBLEM.GET_PROBLEMS_BY_TOPIC}/${topicId}`);
      const result = await response.json();
      
      if (!response.ok) {
        throw new Error(result.message || 'Failed to fetch problems');
      }

      return result;
    } catch (error) {
      console.error('Get problems error:', error);
      throw error;
    }
  }

  // Get problem details
  static async getProblemDetails(problemId: number, language: string = 'python'): Promise<ProblemDetails> {
    try {
      const url = `${API_CONFIG.PROBLEM.GET_PROBLEM_DETAILS}/${problemId}?language=${language}`;
      console.log('Fetching problem details from:', url);
      
      const response = await fetch(url);
      const result = await response.json();
      
      console.log('Problem details response:', result);
      
      if (!response.ok) {
        throw new Error(result.message || 'Failed to fetch problem details');
      }

      return result;
    } catch (error) {
      console.error('Get problem details error:', error);
      throw error;
    }
  }

  // Create contest
  static async createContest(contestData: CreateContestRequest): Promise<ContestDetails> {
    try {
      const userInfo = AuthService.getStoredUserInfo();
      
      if (!userInfo?.userId) {
        throw new Error('No user info found');
      }

      // Transform the data to match backend expectations
      const backendData = {
        name: contestData.title,
        description: contestData.description,
        problemIds: contestData.problemIds,
        durationMinutes: contestData.duration,
        maxParticipants: contestData.maxParticipants,
        isPublic: true
      };

      console.log('=== CONTEST CREATION DEBUG ===');
      console.log('Frontend contest data:', contestData);
      console.log('Backend data being sent:', backendData);
      console.log('API endpoint:', API_CONFIG.CONTEST.CREATE);
      console.log('User ID:', userInfo.userId);
      console.log('Request headers:', {
        'X-User-Id': userInfo.userId.toString(),
        'Content-Type': 'application/json',
      });

      const response = await fetch(API_CONFIG.CONTEST.CREATE, {
        method: 'POST',
        headers: {
          'X-User-Id': userInfo.userId.toString(),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(backendData),
      });

      console.log('Response status:', response.status);
      console.log('Response ok:', response.ok);

      const result = await response.json();
      console.log('Backend response:', result);
      console.log('Response problemIds:', result.problemIds);
      console.log('Response inviteCode:', result.inviteCode);
      
      if (!response.ok) {
        throw new Error(result.message || 'Failed to create contest');
      }

             // Transform the response to match our frontend interface
       return {
         id: result.inviteCode, // Use inviteCode as ID
         title: contestData.title,
         description: contestData.description,
         startTime: new Date().toISOString(),
         endTime: new Date(Date.now() + contestData.duration * 60 * 1000).toISOString(),
         duration: contestData.duration,
         problemIds: contestData.problemIds,
         maxParticipants: contestData.maxParticipants,
         currentParticipants: 1,
         status: 'ONGOING' as const,
         createdBy: userInfo.userId
       };
    } catch (error) {
      console.error('Create contest error:', error);
      throw error;
    }
  }

  // Join contest
  static async joinContest(contestId: string): Promise<any> {
    try {
      const userInfo = AuthService.getStoredUserInfo();
      
      if (!userInfo?.userId) {
        throw new Error('No user info found');
      }

      // First validate that the contest exists
      try {
        await this.getContestDetails(contestId);
      } catch (validateError) {
        throw new Error(`Contest with code "${contestId}" does not exist. Please check the code and try again.`);
      }

      const url = API_CONFIG.CONTEST.JOIN.replace('{contestId}', contestId);
      console.log('Joining contest at:', url);

      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'X-User-Id': userInfo.userId.toString(),
          'Content-Type': 'application/json',
        },
      });

      const result = await response.json();
      console.log('Join contest response:', result);
      console.log('Join contest response details:', {
        status: response.status,
        ok: response.ok,
        problemIds: result.problemIds,
        name: result.name,
        title: result.title,
        description: result.description,
        startTime: result.startTime,
        durationMinutes: result.durationMinutes
      });
      
      if (!response.ok) {
        throw new Error(result.message || 'Failed to join contest');
      }

      return result;
    } catch (error) {
      console.error('Join contest error:', error);
      throw error;
    }
  }

  // Get contest details
  static async getContestDetails(contestId: string): Promise<ContestDetails> {
    try {
      const userInfo = AuthService.getStoredUserInfo();
      
      if (!userInfo?.userId) {
        throw new Error('No user info found');
      }

      const url = API_CONFIG.CONTEST.GET_DETAILS.replace('{contestId}', contestId);
      console.log('Fetching contest details from:', url);

      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'X-User-Id': userInfo.userId.toString(),
          'Content-Type': 'application/json',
        },
      });

      const result = await response.json();
      console.log('Contest details response:', result);
      console.log('Contest details response debug:', {
        status: response.status,
        ok: response.ok,
        problemIds: result.problemIds,
        problemList: result.problemList,
        name: result.name,
        title: result.title,
        description: result.description,
        startTime: result.startTime,
        durationMinutes: result.durationMinutes,
        createdBy: result.createdBy
      });
      
      if (!response.ok) {
        throw new Error(result.message || 'Failed to fetch contest details');
      }

             // Transform the response to match our frontend interface
       const contestDetails = {
         id: contestId,
         title: result.name || result.title,
         description: result.description || '',
         startTime: result.startTime,
         endTime: new Date(new Date(result.startTime).getTime() + result.durationMinutes * 60 * 1000).toISOString(),
         duration: result.durationMinutes,
         problemIds: result.problemIds || result.problemList || [],
         maxParticipants: result.maxParticipants || 50,
         currentParticipants: result.currentParticipants || 1,
         status: 'ONGOING' as const,
         createdBy: result.createdBy || userInfo.userId
       };

      console.log('Transformed contest details:', contestDetails);
      return contestDetails;
    } catch (error) {
      console.error('Get contest details error:', error);
      throw error;
    }
  }

  // Load all selected problems at once
  static async loadSelectedProblems(problemIds: number[]): Promise<ProblemDetails[]> {
    try {
      console.log('Loading selected problems:', problemIds);
      
      const promises = problemIds.map(id => this.getProblemDetails(id));
      const problems = await Promise.all(promises);
      
      console.log('All problems loaded successfully:', problems);
      return problems;
    } catch (error) {
      console.error('Load selected problems error:', error);
      throw error;
    }
  }

  // Run code
  static async runCode(request: CodeExecutionRequest): Promise<CodeExecutionResponse> {
    try {
      console.log('=== CONTEST SERVICE RUN CODE ===');
      console.log('API Endpoint:', API_CONFIG.CONTEST.RUN_CODE);
      console.log('Request payload:', request);
      console.log('Request headers:', { 'Content-Type': 'application/json' });
      
      const startTime = Date.now();
      
      const response = await fetch(API_CONFIG.CONTEST.RUN_CODE, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
      });

      const endTime = Date.now();
      console.log('API call duration:', endTime - startTime, 'ms');
      console.log('Response status:', response.status);
      console.log('Response ok:', response.ok);
      console.log('Response headers:', Object.fromEntries(response.headers.entries()));

      const result = await response.json();
      console.log('=== RUN CODE RESPONSE ===');
      console.log('Full response:', result);
      console.log('Overall verdict:', result.overallVerdict);
      console.log('Test results count:', result.testResults?.length || 0);
      console.log('Error (if any):', result.error);
      
      if (result.testResults) {
        result.testResults.forEach((tr, index) => {
          console.log(`Test ${index + 1}:`, {
            verdict: tr.verdict,
            timeMs: tr.timeMs,
            output: tr.output,
            error: tr.error
          });
        });
      }
      
      if (!response.ok) {
        console.error('API call failed with status:', response.status);
        throw new Error(result.message || `HTTP ${response.status}: Failed to run code`);
      }

      return result;
    } catch (error: any) {
      console.error('=== RUN CODE ERROR ===');
      console.error('Error type:', error.constructor.name);
      console.error('Error message:', error.message);
      console.error('Error stack:', error.stack);
      console.error('Full error object:', error);
      throw error;
    }
  }

  // Submit code
  static async submitCode(request: CodeExecutionRequest): Promise<CodeExecutionResponse> {
    try {
      console.log('=== CONTEST SERVICE SUBMIT CODE ===');
      console.log('API Endpoint:', API_CONFIG.CONTEST.SUBMIT_CODE);
      console.log('Request payload:', request);
      console.log('Request headers:', { 'Content-Type': 'application/json' });
      
      const startTime = Date.now();

      const response = await fetch(API_CONFIG.CONTEST.SUBMIT_CODE, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
      });

      const endTime = Date.now();
      console.log('API call duration:', endTime - startTime, 'ms');
      console.log('Response status:', response.status);
      console.log('Response ok:', response.ok);
      console.log('Response headers:', Object.fromEntries(response.headers.entries()));

      const result = await response.json();
      console.log('=== SUBMIT CODE RESPONSE ===');
      console.log('Full response:', result);
      console.log('Overall verdict:', result.overallVerdict);
      console.log('Test results count:', result.testResults?.length || 0);
      console.log('Error (if any):', result.error);
      
      if (result.testResults) {
        result.testResults.forEach((tr, index) => {
          console.log(`Test ${index + 1}:`, {
            verdict: tr.verdict,
            timeMs: tr.timeMs,
            output: tr.output,
            error: tr.error
          });
        });
      }
      
      if (!response.ok) {
        console.error('API call failed with status:', response.status);
        throw new Error(result.message || `HTTP ${response.status}: Failed to submit code`);
      }

      return result;
    } catch (error: any) {
      console.error('=== SUBMIT CODE ERROR ===');
      console.error('Error type:', error.constructor.name);
      console.error('Error message:', error.message);
      console.error('Error stack:', error.stack);
      console.error('Full error object:', error);
      throw error;
    }
  }
}
