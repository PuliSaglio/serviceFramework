import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import Link from "next/link";

interface CategoriaDTO {
    idCategoria: number;
    nomeCategoria: string;
}

interface Service {
    id: number;
    nome: string;
    descricao: string;
    precoBase: number;
    categoria: CategoriaDTO;
    duracaoEmDias: number; // <-- ADIÇÃO
}


interface Props {
    service: Service;
}

const ServiceCard = ({ service }: Props) => {
    return (
        <Card className="flex flex-col">
            <CardHeader>
                <div className="flex justify-between items-start gap-2">
                    <CardTitle className="text-lg">{service.nome}</CardTitle>
                    <Badge>{service.categoria.nomeCategoria}</Badge>
                </div>
                <CardDescription>{service.descricao}</CardDescription>
            </CardHeader>

            <CardContent>
                <p className="text-2xl font-bold">
                    R$ {service.precoBase.toFixed(2)}
                </p>

                <p className="text-sm text-muted-foreground mt-1">
                    Duração: {service.duracaoEmDias} dia(s)
                </p>
            </CardContent>


            <Link href={`/client/appointments/new?serviceId=${service.id}`} className="w-full">
            <CardFooter>
                <Button className="w-full">
                    Ver detalhes
                </Button>
            </CardFooter>
            </Link>
        </Card>
    );
};

export default ServiceCard;
