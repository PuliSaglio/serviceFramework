'use client';

import { useState, useEffect } from "react";
import { useRouter } from 'next/navigation';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Sheet, SheetContent, SheetHeader, SheetTitle } from "@/components/ui/sheet";
import { Loader2, Pencil, Trash2, Plus } from "lucide-react";
import { z } from "zod";
import LogadoNavbar from "@/components/Professional/LogadoNavbar";
import WhatsAppButton from "@/components/WhatsAppButton";
import { toast } from "sonner";

/* =======================
   SCHEMA
======================= */

const servicoSchema = z.object({
    nome: z.string().min(3).max(100),
    descricao: z.string().min(10).max(500),
    precoBase: z.coerce.number().min(1),
    categoriaId: z.coerce.number(),
    duracaoEmDias: z.coerce.number().min(1),
    nivel: z.enum(["INICIANTE", "INTERMEDIARIO", "AVANCADO"]),
    imagemUrl: z.string().url().nullable().optional(),
});

/* =======================
   TYPES
======================= */

interface CategoriaDTO {
    idCategoria: number;
    nomeCategoria: string;
}

interface Servico {
    id: number;
    nome: string;
    descricao: string;
    precoBase: number;
    duracaoEmDias: number;
    nivel: "INICIANTE" | "INTERMEDIARIO" | "AVANCADO";
    imagemUrl: string | null;
    categoria: CategoriaDTO;
}

interface EditFormDTO {
    nome: string;
    descricao: string;
    precoBase: string;
    categoriaId: string;
    duracaoEmDias: string;
    nivel: "INICIANTE" | "INTERMEDIARIO" | "AVANCADO";
    imagemUrl: string;
}

/* =======================
   CATEGORIAS
======================= */

const categorias: CategoriaDTO[] = [
    { idCategoria: 1, nomeCategoria: "Violão" },
    { idCategoria: 2, nomeCategoria: "Piano" },
    { idCategoria: 3, nomeCategoria: "Flauta" },
    { idCategoria: 4, nomeCategoria: "Bateria" },
    { idCategoria: 5, nomeCategoria: "Canto" },
    { idCategoria: 6, nomeCategoria: "Violino" },
    { idCategoria: 999, nomeCategoria: "Outro" },
];

/* =======================
   PAGE
======================= */

const Page = () => {
    const [servicos, setServicos] = useState<Servico[]>([]);
    const [loading, setLoading] = useState(true);
    const [editingService, setEditingService] = useState<Servico | null>(null);
    const [isEditSheetOpen, setIsEditSheetOpen] = useState(false);
    const [isSaving, setIsSaving] = useState(false);

    const router = useRouter();

    const [editForm, setEditForm] = useState<EditFormDTO>({
        nome: "",
        descricao: "",
        precoBase: "",
        categoriaId: "",
        duracaoEmDias: "",
        nivel: "INICIANTE",
        imagemUrl: "",
    });

    useEffect(() => {
        fetchServicos();
    }, []);

    const getAuthData = () => {
        const authDataString = localStorage.getItem('@servicelink:auth');
        const token = localStorage.getItem('@servicelink:token');
        if (!authDataString || !token) return null;
        try {
            const authData = JSON.parse(authDataString);
            return { prestadorId: authData.profileId, token };
        } catch {
            return null;
        }
    };

    const fetchServicos = async () => {
        const auth = getAuthData();
        if (!auth) return;

        try {
            setLoading(true);
            const res = await fetch(
                `http://localhost:8080/api/servico/prestador/${auth.prestadorId}`,
                { headers: { Authorization: `Bearer ${auth.token}` } }
            );
            if (!res.ok) throw new Error();
            const data = await res.json();
            setServicos(data);
        } catch {
            toast.error("Erro ao carregar serviços");
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (servico: Servico) => {
        setEditingService(servico);
        setEditForm({
            nome: servico.nome,
            descricao: servico.descricao,
            precoBase: servico.precoBase.toString(),
            categoriaId: String(servico.categoria.idCategoria),
            duracaoEmDias: servico.duracaoEmDias.toString(),
            nivel: servico.nivel,
            imagemUrl: servico.imagemUrl ?? "",
        });
        setIsEditSheetOpen(true);
    };

    const handleSaveEdit = async () => {
        if (!editingService) return;

        const payload = {
            nome: editForm.nome,
            descricao: editForm.descricao,
            precoBase: editForm.precoBase,
            categoriaId: editForm.categoriaId,
            duracaoEmDias: editForm.duracaoEmDias,
            nivel: editForm.nivel,
            imagemUrl: editForm.imagemUrl || null,
        };

        try {
            const validated = servicoSchema.parse(payload);
            const auth = getAuthData();
            if (!auth) return;

            setIsSaving(true);

            const res = await fetch(
                `http://localhost:8080/api/servico/${editingService.id}`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${auth.token}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(validated),
                }
            );

            if (!res.ok) throw new Error();

            const updated = await res.json();
            setServicos(prev =>
                prev.map(s => (s.id === updated.id ? updated : s))
            );

            toast.success("Serviço atualizado");
            setIsEditSheetOpen(false);
        } catch {
            toast.error("Erro ao salvar serviço");
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <div className="min-h-screen bg-background">
            <LogadoNavbar />

            <main className="container mx-auto px-4 py-8">
                <div className="flex justify-between mb-8">
                    <h1 className="text-4xl font-bold">Meus Serviços</h1>
                    <Button onClick={() => router.push("/professional/services/add")}>
                        <Plus className="h-4 w-4 mr-2" /> Novo Serviço
                    </Button>
                </div>

                {loading ? (
                    <Loader2 className="animate-spin mx-auto" />
                ) : (
                    <div className="grid md:grid-cols-3 gap-6">
                        {servicos.map(servico => (
                            <Card key={servico.id}>
                                <CardHeader>
                                    <div className="flex justify-between">
                                        <CardTitle>{servico.nome}</CardTitle>
                                        <Badge>{servico.categoria.nomeCategoria}</Badge>
                                    </div>
                                    <CardDescription>{servico.descricao}</CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-1">
                                    <p className="font-bold">
                                        R$ {servico.precoBase.toFixed(2)}
                                    </p>
                                    <p>Duração: {servico.duracaoEmDias} dias</p>
                                    <p>Nível: {servico.nivel}</p>
                                </CardContent>
                                <CardFooter className="gap-2">
                                    <Button variant="outline" onClick={() => handleEdit(servico)}>
                                        <Pencil className="h-4 w-4" />
                                    </Button>
                                    <Button variant="destructive">
                                        <Trash2 className="h-4 w-4" />
                                    </Button>
                                </CardFooter>
                            </Card>
                        ))}
                    </div>
                )}
            </main>

            <Sheet open={isEditSheetOpen} onOpenChange={setIsEditSheetOpen}>
                <SheetContent>
                    <SheetHeader>
                        <SheetTitle>Editar Serviço</SheetTitle>
                    </SheetHeader>

                    <div className="space-y-4 mt-4">
                        <Input value={editForm.nome} onChange={e => setEditForm({ ...editForm, nome: e.target.value })} />
                        <Textarea value={editForm.descricao} onChange={e => setEditForm({ ...editForm, descricao: e.target.value })} />
                        <Input type="number" value={editForm.precoBase} onChange={e => setEditForm({ ...editForm, precoBase: e.target.value })} />
                        <Input type="number" value={editForm.duracaoEmDias} onChange={e => setEditForm({ ...editForm, duracaoEmDias: e.target.value })} />

                        <Select value={editForm.nivel} onValueChange={v => setEditForm({ ...editForm, nivel: v as any })}>
                            <SelectTrigger><SelectValue /></SelectTrigger>
                            <SelectContent>
                                <SelectItem value="INICIANTE">Iniciante</SelectItem>
                                <SelectItem value="INTERMEDIARIO">Intermediário</SelectItem>
                                <SelectItem value="AVANCADO">Avançado</SelectItem>
                            </SelectContent>
                        </Select>

                        <Select value={editForm.categoriaId} onValueChange={v => setEditForm({ ...editForm, categoriaId: v })}>
                            <SelectTrigger><SelectValue /></SelectTrigger>
                            <SelectContent>
                                {categorias.map(c => (
                                    <SelectItem key={c.idCategoria} value={String(c.idCategoria)}>
                                        {c.nomeCategoria}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>

                        <Button onClick={handleSaveEdit} disabled={isSaving}>
                            {isSaving ? <Loader2 className="animate-spin" /> : "Salvar"}
                        </Button>
                    </div>
                </SheetContent>
            </Sheet>

            <WhatsAppButton />
        </div>
    );
};

export default Page;
