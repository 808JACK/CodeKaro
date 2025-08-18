# Dashboard Implementation Guide

## 🏠 Dashboard Flow Overview

### 1. **User Login & Dashboard Access**
```
User logs in → Redirected to http://localhost:4000/ → Dashboard loads recent contest rooms
```

### 2. **API Endpoints**

#### **Get Recent Rooms (Dashboard)**
```
GET /api/contests/recent-rooms
Headers: X-User-Id: {userId}
```
**Response:** Top 5 recent contest rooms user participated in
```json
[
  {
    "id": "B0Q95PCG",           // Contest code for joining
    "name": "Algorithm Contest", // Contest name from PostgreSQL
    "type": "contest",
    "participants": 15,          // Total unique participants
    "lastActive": "2 hours ago", // User's last activity
    "createdAt": "2025-08-17T13:30:22.257+0530"
  }
]
```

#### **Get Room Details (When User Clicks)**
```
GET /api/contests/room/{contestCode}/dashboard-data
Headers: X-User-Id: {userId}
```
**Response:** Complete contest + user submission data
```json
{
  "contestId": 1,
  "contestCode": "B0Q95PCG",
  "contestName": "Algorithm Contest",
  "description": "Practice algorithms",
  "startTime": "2025-08-17T13:30:22.257+0530",
  "endTime": "2025-08-17T14:30:22.257+0530",
  "durationMinutes": 60,
  "problemIds": [1, 2, 3],
  "userSubmissions": [...],      // All user's submissions from MongoDB
  "totalUserSubmissions": 5,
  "userAcceptedSubmissions": 2,
  "userLastActivity": "2 hours ago",
  "totalParticipants": 15
}
```

#### **Get User Submissions (Detailed View)**
```
GET /api/contests/{contestCode}/submissions
Headers: X-User-Id: {userId}
```
**Response:** User's submissions for specific contest from MongoDB

### 3. **Frontend Implementation**

#### **Dashboard Component (http://localhost:4000/)**
```tsx
import React, { useEffect, useState } from 'react';
import { dashboardService, RecentRoom } from '../services/dashboardService';

const Dashboard: React.FC = () => {
  const [recentRooms, setRecentRooms] = useState<RecentRoom[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadRecentRooms();
  }, []);

  const loadRecentRooms = async () => {
    try {
      const rooms = await dashboardService.getRecentRooms();
      setRecentRooms(rooms);
    } catch (error) {
      console.error('Failed to load recent rooms:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRoomClick = async (contestCode: string) => {
    try {
      // Load detailed room data
      const roomData = await dashboardService.getContestRoomData(contestCode);
      
      // Navigate to contest room or show detailed view
      // You can either:
      // 1. Navigate to contest room: /contests/{contestCode}
      // 2. Show modal with submission history
      // 3. Navigate to dedicated room details page
      
      console.log('Room data:', roomData);
      // Navigate or show details...
    } catch (error) {
      console.error('Failed to load room data:', error);
    }
  };

  if (loading) return <div>Loading recent rooms...</div>;

  return (
    <div className="dashboard">
      <h1>Your Recent Contest Rooms</h1>
      
      {recentRooms.length === 0 ? (
        <div className="no-rooms">
          <p>You haven't participated in any contests yet.</p>
          <button onClick={() => window.location.href = '/contests/create'}>
            Create Your First Contest
          </button>
        </div>
      ) : (
        <div className="recent-rooms-grid">
          {recentRooms.map((room) => (
            <div 
              key={room.id} 
              className="room-card"
              onClick={() => handleRoomClick(room.id)}
            >
              <h3>{room.name}</h3>
              <div className="room-stats">
                <span>👥 {room.participants} participants</span>
                <span>🕒 {room.lastActive}</span>
              </div>
              <div className="room-code">Code: {room.id}</div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Dashboard;
```

### 4. **Data Flow Architecture**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Dashboard     │    │   Backend API    │    │   Databases     │
│  (Frontend)     │    │                  │    │                 │
├─────────────────┤    ├──────────────────┤    ├─────────────────┤
│ 1. Load recent  │───▶│ GET /recent-rooms│───▶│ MongoDB: Find   │
│    rooms        │    │                  │    │ user submissions│
│                 │    │ 2. Get contest   │───▶│ PostgreSQL: Get │
│ 2. User clicks  │───▶│    IDs from      │    │ contest metadata│
│    room         │    │    submissions   │    │                 │
│                 │    │                  │    │ Return: Top 5   │
│ 3. Show room    │◀───│ 3. Load contest  │◀───│ recent contests │
│    details +    │    │    metadata +    │    │ with user data  │
│    submissions  │    │    submissions   │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### 5. **Key Features**

#### ✅ **Dashboard Features:**
- **Top 5 Recent Rooms** - Based on user's MongoDB submissions
- **Contest Metadata** - Name, participants, timing from PostgreSQL  
- **User Activity** - Last submission time, submission count
- **Quick Access** - Click to rejoin contest or view submissions

#### ✅ **Room Details Features:**
- **Complete Contest Info** - Description, timing, problems
- **User Submission History** - All submissions from MongoDB
- **Statistics** - Accepted/total submissions, participant count
- **Quick Actions** - Rejoin contest, view code, etc.

### 6. **Backend Processing**

1. **MongoDB Query** - Find contests where user has submissions
2. **PostgreSQL Query** - Get contest metadata for those contest IDs
3. **Data Combination** - Merge contest info + user submission stats
4. **Sorting** - Order by user's most recent activity
5. **Limiting** - Return top 5 for dashboard performance

### 7. **Testing the Implementation**

```bash
# Test recent rooms endpoint
curl -H "X-User-Id: 123" http://localhost:8092/api/contests/recent-rooms

# Test room details endpoint  
curl -H "X-User-Id: 123" http://localhost:8092/api/contests/room/B0Q95PCG/dashboard-data

# Test user submissions endpoint
curl -H "X-User-Id: 123" http://localhost:8092/api/contests/B0Q95PCG/submissions
```

This implementation provides a complete dashboard experience where users can:
1. **See their recent contest activity** immediately after login
2. **Click on any room** to see detailed submission history
3. **Quickly rejoin contests** or review their performance
4. **Get insights** into their contest participation patterns

The system efficiently combines PostgreSQL contest metadata with MongoDB submission data to provide a rich user experience.