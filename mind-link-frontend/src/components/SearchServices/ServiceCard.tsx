import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import Link from "next/link";

interface ServiceCategory {
    id: number;
    nome: string;
}

interface Service {
    id: number;
    nome: string;
    descricao: string;
    precoBase: number;
    categoria: ServiceCategory;
}

interface ServiceCardProps {
  service: Service;
}

const ServiceCard = ({ service }: ServiceCardProps) => {
  return (
    <Card className="flex flex-col h-full hover:shadow-hover transition-shadow">
      <CardHeader>
        <div className="flex items-start justify-between gap-2 mb-2">
          <CardTitle className="text-xl">{service.nome}</CardTitle>
          <Badge variant="secondary" className="shrink-0">
            {service.categoria.nome}
          </Badge>
        </div>
        <CardDescription>{service.descricao}</CardDescription>
      </CardHeader>
      <CardContent className="flex-grow">
        <div className="text-2xl font-bold text-primary">
          R${service.precoBase}
        </div>
      </CardContent>
      <CardFooter>
          <Link href={`/client/appointments/new?serviceId=${service.id}`} className="w-full">
        <Button className="w-full">

                Contrate Já

        </Button>
          </Link>
      </CardFooter>
    </Card>
  );
};

export default ServiceCard;
