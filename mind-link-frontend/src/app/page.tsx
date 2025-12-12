import { Button } from "@/components/ui/button";
import { Users, Briefcase, Star, Shield, Clock } from "lucide-react";
import Link from "next/link";
import heroImage from "@/assets/hero-image.png";
import Image from "next/image";

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-hero">
      {/* Header */}
      <header className="container mx-auto px-4 py-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Briefcase className="h-8 w-8 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">Mind Link</h1>
          </div>
          <nav className="hidden md:flex gap-4">
            <Button variant="ghost" asChild>
              <Link href="/login">Entrar</Link>
            </Button>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section className="container mx-auto px-4 py-16 md:py-24">
        <div className="grid md:grid-cols-2 gap-12 items-center">
          <div className="space-y-8 animate-fade-in">
            <div className="space-y-4">
              <h2 className="text-4xl md:text-5xl font-bold text-foreground leading-tight">
                  Encontre o psicólogo ideal para você
              </h2>
              <p className="text-lg text-muted-foreground">
                  Cuide da sua mente com especialistas verificados. De terapia cognitivo-comportamental a psicanálise, conectamos você ao profissional ideal.
              </p>
            </div>

            <div className="flex flex-col sm:flex-row gap-4">
              <Button size="lg" className="text-lg h-14" asChild>
                <Link href="/register/client">
                  <Users className="mr-2 h-5 w-5" />
                  Sou Cliente
                </Link>
              </Button>
              <Button
                size="lg"
                variant="outline"
                className="text-lg h-14 border-2"
                asChild
              >
                <Link href="/register/professional">
                  <Briefcase className="mr-2 h-5 w-5" />
                  Sou Psicólogo
                </Link>
              </Button>
            </div>

            {/* Trust indicators */}
            <div className="flex flex-wrap gap-6 pt-4">
              <div className="flex items-center gap-2 text-muted-foreground">
                <Shield className="h-5 w-5 text-primary" />
                <span className="text-sm">Psicólogos verificados</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Star className="h-5 w-5 text-primary" />
                <span className="text-sm">Avaliações reais</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Clock className="h-5 w-5 text-primary" />
                <span className="text-sm">Agendamento rápido</span>
              </div>
            </div>
          </div>

          <div className="relative animate-slide-up">
            <div >
              <Image
                src={heroImage}
                alt="Profissionais prestando serviços"
                className="w-full h-auto"
              />
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
