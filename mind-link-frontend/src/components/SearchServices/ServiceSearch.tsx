import { useState } from "react";
import { Search, RotateCcw } from "lucide-react"; // Importando o ícone para Limpar
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

interface ServiceSearchProps {
    onFilter: (searchTerm: string, minPrice: string, maxPrice: string, category: string) => void;
}

const ServiceSearch = ({ onFilter }: ServiceSearchProps) => {
    const [searchTerm, setSearchTerm] = useState("");
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");
    const [category, setCategory] = useState("all");

    const handleApplyFilters = () => {
        onFilter(searchTerm, minPrice, maxPrice, category);
    };

    /**
     * Função para Limpar Filtros:
     * 1. Redefine todos os estados locais para seus valores iniciais.
     * 2. Chama onFilter com os valores limpos para buscar todos os serviços.
     */
    const handleClearFilters = () => {
        // 1. Redefine os estados locais
        setSearchTerm("");
        setMinPrice("");
        setMaxPrice("");
        setCategory("all");

        // 2. Chama a função de filtro com valores vazios/padrão
        onFilter("", "", "", "all");
    };

    // Determina se algum filtro está ativo para habilitar/desabilitar o botão de limpar
    const isAnyFilterActive =
        searchTerm !== "" ||
        minPrice !== "" ||
        maxPrice !== "" ||
        category !== "all";

    return (
        <div className="bg-card border border-border rounded-lg p-6 shadow-card">
            <div className="space-y-4">
                {/* Campo de Busca */}
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-5 h-5" />
                    <Input
                        type="text"
                        placeholder="Buscar por serviço..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="pl-10"
                    />
                </div>

                {/* Filtros de Preço e Categoria */}
                <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
                    {/* Mudamos para grid-cols-5 para acomodar o botão de Limpar */}

                    {/* Preço Mínimo */}
                    <div>
                        <label className="text-sm font-medium text-foreground mb-2 block">
                            Preço Mínimo
                        </label>
                        <Input
                            type="number"
                            placeholder="R$ 0"
                            value={minPrice}
                            onChange={(e) => setMinPrice(e.target.value)}
                            min="0"
                        />
                    </div>

                    {/* Preço Máximo */}
                    <div>
                        <label className="text-sm font-medium text-foreground mb-2 block">
                            Preço Máximo
                        </label>
                        <Input
                            type="number"
                            placeholder="R$ 1000"
                            value={maxPrice}
                            onChange={(e) => setMaxPrice(e.target.value)}
                            min="0"
                        />
                    </div>

                    {/* Categoria */}
                    <div>
                        <label className="text-sm font-medium text-foreground mb-2 block">
                            Categoria
                        </label>
                        <Select value={category} onValueChange={setCategory}>
                            <SelectTrigger>
                                <SelectValue placeholder="Selecione..." />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">Todas</SelectItem>
                                <SelectItem value="1">Cognitivo-comportamental</SelectItem>
                                <SelectItem value="2">Psicanálise</SelectItem>
                                <SelectItem value="3">Humanista</SelectItem>
                                <SelectItem value="4">Comportamentais clássicas</SelectItem>
                                <SelectItem value="5">Sistêmica</SelectItem>
                                <SelectItem value="6">EMDR</SelectItem>
                                <SelectItem value="7">Interpessoal</SelectItem>
                                <SelectItem value="999">Geral</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>

                    <div className="flex items-end">
                        <Button
                            onClick={handleClearFilters}
                            className="w-full bg-secondary text-secondary-foreground hover:bg-secondary/80"
                            variant="outline" // Usando outline para destacar
                            disabled={!isAnyFilterActive} // Desabilita se não houver filtro ativo
                        >
                            <RotateCcw className="w-4 h-4 mr-2" />
                            Limpar
                        </Button>
                    </div>

                    <div className="flex items-end">
                        <Button onClick={handleApplyFilters} className="w-full">
                            Aplicar Filtros
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ServiceSearch;