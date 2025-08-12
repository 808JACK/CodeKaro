import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Code, Users, Trophy, Zap, Star, ArrowRight, Github, Play } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Landing = () => {
  const navigate = useNavigate();
  const [animatedElements, setAnimatedElements] = useState<number[]>([]);

  useEffect(() => {
    // Animate elements in sequence
    const timeouts = [
      setTimeout(() => setAnimatedElements(prev => [...prev, 0]), 100),
      setTimeout(() => setAnimatedElements(prev => [...prev, 1]), 300),
      setTimeout(() => setAnimatedElements(prev => [...prev, 2]), 500),
      setTimeout(() => setAnimatedElements(prev => [...prev, 3]), 700),
    ];

    return () => timeouts.forEach(clearTimeout);
  }, []);

  const features = [
    {
      icon: Code,
      title: "Real-time Collaboration",
      description: "Code together with your team in real-time, just like Google Docs",
      gradient: "from-primary to-accent"
    },
    {
      icon: Users,
      title: "Team Communication",
      description: "Integrated chat and voice communication while coding",
      gradient: "from-accent to-info"
    },
    {
      icon: Trophy,
      title: "Competitive Contests",
      description: "Host and participate in timed coding competitions",
      gradient: "from-warning to-destructive"
    }
  ];

  const stats = [
    { number: "10K+", label: "Active Users" },
    { number: "500+", label: "Problems Solved" },
    { number: "100+", label: "Contests Hosted" },
    { number: "50+", label: "Teams Formed" }
  ];

  return (
    <div className="min-h-screen bg-background overflow-hidden">
      {/* Animated Background */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-primary/20 rounded-full blur-3xl animate-pulse-glow"></div>
        <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-accent/20 rounded-full blur-3xl animate-pulse-glow" style={{ animationDelay: '1s' }}></div>
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-warning/10 rounded-full blur-3xl animate-pulse-glow" style={{ animationDelay: '2s' }}></div>
      </div>

      {/* Header */}
      <header className="relative z-10 border-b border-border/50 bg-background/80 backdrop-blur-sm">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-gradient-primary rounded-lg animate-pulse-glow">
                <Code className="h-6 w-6 text-primary-foreground" />
              </div>
              <div>
                <h1 className="text-2xl font-bold bg-gradient-primary bg-clip-text text-transparent">
                  CodeCollab
                </h1>
                <p className="text-xs text-muted-foreground">Collaborative Coding Platform</p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <Button 
                variant="ghost" 
                onClick={() => navigate('/login')}
                className="hover:bg-primary/10"
              >
                Sign In
              </Button>
              <Button 
                onClick={() => navigate('/signup')}
                className="bg-gradient-primary hover:opacity-90 transition-opacity"
              >
                Get Started
                <ArrowRight className="ml-2 h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </header>

      <main className="relative z-10">
        {/* Hero Section */}
        <section className="container mx-auto px-6 py-20">
          <div className="text-center space-y-8 max-w-4xl mx-auto">
            <div className={`space-y-4 transition-all duration-1000 ${animatedElements.includes(0) ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-10'}`}>
              <h1 className="text-6xl md:text-8xl font-bold leading-tight">
                Code{' '}
                <span className="bg-gradient-primary bg-clip-text text-transparent">
                  Together
                </span>
                <br />
                <span className="text-muted-foreground text-4xl md:text-6xl">
                  Build Better
                </span>
              </h1>
            </div>

            <div className={`space-y-6 transition-all duration-1000 delay-200 ${animatedElements.includes(1) ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-10'}`}>
              <p className="text-xl md:text-2xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
                The most advanced collaborative coding platform for teams. 
                Real-time editing, integrated communication, and competitive programming.
              </p>
              
              <div className="flex flex-wrap justify-center gap-4 pt-8">
                <Button 
                  size="lg" 
                  onClick={() => navigate('/signup')}
                  className="bg-gradient-primary hover:opacity-90 transition-all duration-300 text-lg px-8 py-6 hover:shadow-glow"
                >
                  <Play className="mr-2 h-5 w-5" />
                  Start Coding Now
                </Button>
                <Button 
                  size="lg" 
                  variant="outline"
                  className="text-lg px-8 py-6 border-primary/30 hover:bg-primary/10"
                >
                  <Github className="mr-2 h-5 w-5" />
                  View Demo
                </Button>
              </div>
            </div>

            {/* Stats */}
            <div className={`grid grid-cols-2 md:grid-cols-4 gap-8 pt-16 transition-all duration-1000 delay-400 ${animatedElements.includes(2) ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-10'}`}>
              {stats.map((stat, index) => (
                <div key={index} className="text-center">
                  <div className="text-3xl md:text-4xl font-bold bg-gradient-primary bg-clip-text text-transparent">
                    {stat.number}
                  </div>
                  <div className="text-muted-foreground mt-2">{stat.label}</div>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* Features Section */}
        <section className="container mx-auto px-6 py-20">
          <div className={`text-center mb-16 transition-all duration-1000 delay-600 ${animatedElements.includes(3) ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-10'}`}>
            <h2 className="text-4xl md:text-5xl font-bold mb-6">
              Everything you need to{' '}
              <span className="bg-gradient-accent bg-clip-text text-transparent">
                collaborate
              </span>
            </h2>
            <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
              From real-time editing to competitive programming, we've got all the tools
              your team needs to code effectively together.
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <div 
                key={index}
                className={`group p-8 rounded-2xl bg-gradient-card border border-border/50 hover:border-primary/30 transition-all duration-500 hover:shadow-glow cursor-pointer transform hover:scale-105 ${
                  animatedElements.includes(3) ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-20'
                }`}
                style={{ transitionDelay: `${800 + index * 200}ms` }}
              >
                <div className={`w-16 h-16 rounded-xl bg-gradient-to-br ${feature.gradient} p-4 mb-6 group-hover:animate-pulse-glow`}>
                  <feature.icon className="h-8 w-8 text-white" />
                </div>
                <h3 className="text-2xl font-semibold mb-4 group-hover:text-primary transition-colors">
                  {feature.title}
                </h3>
                <p className="text-muted-foreground leading-relaxed">
                  {feature.description}
                </p>
              </div>
            ))}
          </div>
        </section>

        {/* CTA Section */}
        <section className="container mx-auto px-6 py-20">
          <div className="bg-gradient-card rounded-3xl border border-border/50 p-12 text-center relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-primary opacity-5"></div>
            <div className="relative z-10">
              <Zap className="h-16 w-16 mx-auto mb-6 text-primary animate-pulse-glow" />
              <h2 className="text-4xl md:text-5xl font-bold mb-6">
                Ready to start coding{' '}
                <span className="bg-gradient-primary bg-clip-text text-transparent">
                  together?
                </span>
              </h2>
              <p className="text-xl text-muted-foreground mb-8 max-w-2xl mx-auto">
                Join thousands of developers who are already collaborating and competing on CodeCollab.
              </p>
              <div className="flex flex-wrap justify-center gap-4">
                <Button 
                  size="lg" 
                  onClick={() => navigate('/signup')}
                  className="bg-gradient-primary hover:opacity-90 transition-all duration-300 text-lg px-8 py-6 hover:shadow-glow"
                >
                  <Star className="mr-2 h-5 w-5" />
                  Join Free Today
                </Button>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="border-t border-border/50 bg-background/80 backdrop-blur-sm mt-20">
        <div className="container mx-auto px-6 py-8">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-gradient-primary rounded-lg">
                <Code className="h-5 w-5 text-primary-foreground" />
              </div>
              <div>
                <div className="font-semibold">CodeCollab</div>
                <div className="text-sm text-muted-foreground">© 2024 All rights reserved</div>
              </div>
            </div>
            <div className="text-sm text-muted-foreground">
              Built with ❤️ for developers
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Landing;