import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { 
  Users, 
  MessageCircle, 
  Send,
  Trophy,
  Volume2,
  Mic,
  X
} from 'lucide-react';

interface RoomSidebarProps {
  isOpen: boolean;
  onClose: () => void;
  activeTab: 'participants' | 'chat' | 'leaderboard';
  onTabChange: (tab: 'participants' | 'chat' | 'leaderboard') => void;
  participants: Array<{
    id: string;
    name: string;
    avatar: string;
    isOnline: boolean;
    cursor?: { line: number; col: number } | null;
  }>;
  chatMessages: Array<{
    id: string;
    user: string;
    message: string;
    timestamp: string;
  }>;
  leaderboard?: Array<{
    rank: number;
    name: string;
    score: number;
    problems: number;
    time: string;
  }>;
  isContest: boolean;
  onSendMessage: (message: string) => void;
  isLoading?: boolean;
}

export const RoomSidebar = ({
  isOpen,
  onClose,
  activeTab,
  onTabChange,
  participants,
  chatMessages,
  leaderboard = [],
  isContest,
  onSendMessage,
  isLoading = false
}: RoomSidebarProps) => {
  const [chatMessage, setChatMessage] = useState('');
  const [isRecording, setIsRecording] = useState(false);

  const handleSendMessage = () => {
    if (!chatMessage.trim() || isLoading) return;
    onSendMessage(chatMessage);
    setChatMessage('');
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-y-0 right-0 w-80 bg-card/95 backdrop-blur-sm border-l border-border shadow-xl z-50 flex flex-col animate-slide-in-right">
      {/* Header */}
      <div className="flex items-center justify-between p-4 border-b border-border">
        <h3 className="font-semibold">Room Info</h3>
        <Button variant="ghost" size="sm" onClick={onClose}>
          <X className="h-4 w-4" />
        </Button>
      </div>

      {/* Tabs */}
      <Tabs value={activeTab} onValueChange={(value) => onTabChange(value as any)} className="flex-1 flex flex-col">
        <TabsList className={`grid w-full m-2 ${isContest ? 'grid-cols-2' : 'grid-cols-3'}`}>
          <TabsTrigger value="participants" className="flex items-center gap-1">
            <Users className="h-4 w-4" />
            <span className="hidden sm:inline">People</span>
          </TabsTrigger>
          {!isContest && (
            <TabsTrigger value="chat" className="flex items-center gap-1">
              <MessageCircle className="h-4 w-4" />
              <span className="hidden sm:inline">Chat</span>
            </TabsTrigger>
          )}
          {isContest && (
            <TabsTrigger value="leaderboard" className="flex items-center gap-1">
              <Trophy className="h-4 w-4" />
              <span className="hidden sm:inline">Board</span>
            </TabsTrigger>
          )}
        </TabsList>

        {/* Participants Tab */}
        <TabsContent value="participants" className="flex-1 p-4 space-y-3 overflow-y-auto">
          <div className="space-y-3">
            {participants.map((participant) => (
              <div 
                key={participant.id}
                className="flex items-center gap-3 p-3 rounded-lg hover:bg-secondary/30 transition-colors"
              >
                <span className="text-lg">{participant.avatar}</span>
                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <span className="font-medium">{participant.name}</span>
                    <div className={`
                      w-2 h-2 rounded-full
                      ${participant.isOnline ? 'bg-success' : 'bg-muted-foreground'}
                    `} />
                  </div>
                  {participant.cursor && participant.isOnline && (
                    <div className="text-xs text-muted-foreground">
                      Line {participant.cursor.line}, Col {participant.cursor.col}
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        </TabsContent>

        {/* Chat Tab */}
        <TabsContent value="chat" className="flex-1 flex flex-col">
          <div className="flex-1 p-4 space-y-3 overflow-y-auto">
            {chatMessages.map((msg) => (
              <div key={msg.id} className="space-y-1">
                <div className="flex items-center gap-2 text-xs text-muted-foreground">
                  <span className="font-medium">{msg.user}</span>
                  <span>{msg.timestamp}</span>
                </div>
                <div className="bg-secondary/30 rounded-lg p-3 text-sm">
                  {msg.message}
                </div>
              </div>
            ))}
          </div>
          
          <div className="p-4 border-t border-border">
            <div className="flex gap-2">
              <Input
                placeholder="Type a message..."
                value={chatMessage}
                onChange={(e) => setChatMessage(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
                className="flex-1"
                disabled={isLoading}
              />
              <Button size="sm" onClick={handleSendMessage} disabled={isLoading}>
                <Send className="h-4 w-4" />
              </Button>
            </div>
            <div className="flex items-center gap-2 mt-2">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setIsRecording(!isRecording)}
                className={isRecording ? 'bg-destructive/20 text-destructive' : ''}
              >
                <Mic className="h-4 w-4" />
              </Button>
              <Button variant="ghost" size="sm">
                <Volume2 className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </TabsContent>

        {/* Leaderboard Tab */}
        {isContest && (
          <TabsContent value="leaderboard" className="flex-1 p-4 space-y-3 overflow-y-auto">
            <div className="space-y-2">
              {leaderboard.map((entry) => (
                <Card key={entry.rank} className="p-3">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className={`
                        w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold
                        ${entry.rank === 1 ? 'bg-warning text-warning-foreground' : 'bg-secondary'}
                      `}>
                        {entry.rank}
                      </div>
                      <span className="font-medium">{entry.name}</span>
                    </div>
                    <div className="text-right text-sm">
                      <div className="font-semibold">{entry.score}pts</div>
                      <div className="text-muted-foreground">{entry.time}</div>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          </TabsContent>
        )}
      </Tabs>
    </div>
  );
};