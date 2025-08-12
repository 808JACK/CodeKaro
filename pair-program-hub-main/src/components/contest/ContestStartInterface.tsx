import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { 
  Trophy, 
  Timer, 
  Code, 
  Users, 
  Play, 
  ArrowLeft, 
  Crown 
} from 'lucide-react';
import { ContestDetails, ProblemDetails } from '@/services/contestService';

interface ContestStartInterfaceProps {
  contest: ContestDetails;
  problems: ProblemDetails[];
  isAdmin: boolean;
  onStartContest: () => void;
  onBack: () => void;
}

export const ContestStartInterface = ({
  contest,
  problems,
  isAdmin,
  onStartContest,
  onBack
}: ContestStartInterfaceProps) => {
  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'EASY': return 'bg-green-500';
      case 'MEDIUM': return 'bg-yellow-500';
      case 'HARD': return 'bg-red-500';
      default: return 'bg-gray-500';
    }
  };

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm">
        <div className="flex items-center justify-between px-4 py-3">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              onClick={onBack}
              className="gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              Back
            </Button>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-warning/10 rounded-lg">
                <Trophy className="h-5 w-5 text-warning" />
              </div>
              <div>
                <h1 className="text-lg font-bold">{contest.title}</h1>
                <p className="text-sm text-muted-foreground">Room: {contest.id}</p>
              </div>
            </div>
          </div>
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <Crown className="h-4 w-4" />
            <span>{isAdmin ? 'Admin' : 'Participant'}</span>
          </div>
        </div>
      </header>

      {/* Contest Start Interface */}
      <div className="container mx-auto px-4 py-8">
        <div className="max-w-4xl mx-auto">
          {/* Contest Info Card */}
          <div className="bg-gradient-to-br from-warning/10 via-background to-background border border-warning/20 rounded-xl p-8 mb-8">
            <div className="text-center mb-8">
              <div className="inline-flex items-center justify-center w-16 h-16 bg-warning/10 rounded-full mb-4">
                <Trophy className="h-8 w-8 text-warning" />
              </div>
              <h1 className="text-3xl font-bold mb-2">{contest.title}</h1>
              <p className="text-muted-foreground text-lg">{contest.description}</p>
            </div>

            <div className="grid md:grid-cols-3 gap-6 mb-8">
              <div className="text-center p-4 bg-card/50 rounded-lg border">
                <div className="inline-flex items-center justify-center w-12 h-12 bg-blue-500/10 rounded-full mb-3">
                  <Timer className="h-6 w-6 text-blue-500" />
                </div>
                <h3 className="font-semibold mb-1">Duration</h3>
                <p className="text-2xl font-bold text-blue-500">{contest.duration} min</p>
              </div>

              <div className="text-center p-4 bg-card/50 rounded-lg border">
                <div className="inline-flex items-center justify-center w-12 h-12 bg-green-500/10 rounded-full mb-3">
                  <Code className="h-6 w-6 text-green-500" />
                </div>
                <h3 className="font-semibold mb-1">Problems</h3>
                <p className="text-2xl font-bold text-green-500">{problems.length}</p>
              </div>

              <div className="text-center p-4 bg-card/50 rounded-lg border">
                <div className="inline-flex items-center justify-center w-12 h-12 bg-purple-500/10 rounded-full mb-3">
                  <Users className="h-6 w-6 text-purple-500" />
                </div>
                <h3 className="font-semibold mb-1">Max Participants</h3>
                <p className="text-2xl font-bold text-purple-500">{contest.maxParticipants}</p>
              </div>
            </div>

            {/* Problem List */}
            <div className="mb-8">
              <h3 className="text-xl font-semibold mb-4 text-center">Contest Problems</h3>
              <div className="grid gap-3">
                {problems.map((problem, index) => (
                  <div key={problem.id} className="flex items-center justify-between p-4 bg-card/30 rounded-lg border">
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 bg-warning/10 rounded-full flex items-center justify-center text-sm font-semibold">
                        {index + 1}
                      </div>
                      <div>
                        <h4 className="font-medium">{problem.title}</h4>
                        <p className="text-sm text-muted-foreground">
                          Topics: {problem.topicIds.join(', ')}
                        </p>
                      </div>
                    </div>
                    <Badge className={getDifficultyColor(problem.difficulty)}>
                      {problem.difficulty}
                    </Badge>
                  </div>
                ))}
              </div>
            </div>

            {/* Start Contest Button */}
            <div className="text-center">
              <Button
                onClick={onStartContest}
                size="lg"
                className="bg-warning hover:bg-warning/90 text-warning-foreground px-8 py-3 text-lg font-semibold"
              >
                Start Contest
                <Play className="ml-2 h-5 w-5" />
              </Button>
              <p className="text-sm text-muted-foreground mt-2">
                Click to begin your coding challenge
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};