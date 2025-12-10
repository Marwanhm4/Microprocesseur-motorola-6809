# Modes d'Adressage du Motorola 6809

## 📚 Différence entre les Modes

### 1. Mode IMMÉDIAT (#imm)

**Caractéristiques :**
- ✅ La valeur est **directement dans l'instruction**
- ✅ **N'utilise PAS le registre DP**
- ✅ **N'accède PAS à la mémoire** (pour lire la valeur)
- ✅ Très rapide (pas de calcul d'adresse)

**Syntaxe assembleur :**
```
LDA #$42    ; Charge la valeur 0x42 directement dans A
ADDA #10    ; Ajoute 10 directement à A
```

**Exemples d'opcodes :**
- `0x86` : `LDA #imm` - Charge valeur immédiate dans A
- `0xC6` : `LDB #imm` - Charge valeur immédiate dans B
- `0x8B` : `ADDA #imm` - Ajoute valeur immédiate à A

**Format en mémoire :**
```
[Opcode] [Valeur 8 bits]
  0x86      0x42
```

**Implémentation actuelle :**
```java
private void opLDAimmediate() {
    // Mode IMMEDIAT: valeur directement dans l'instruction
    // Ne nécessite PAS le registre DP
    regA = fetchByte();  // Lit la valeur directement
    updateNZFlags(regA);
}
```

---

### 2. Mode DIRECT PAGE (<addr)

**Caractéristiques :**
- ✅ Utilise le **registre DP** (Direct Page)
- ✅ Adresse effective = `(DP << 8) | offset`
- ✅ Permet d'adresser 256 bytes dans une "page" de 64 Ko
- ✅ Offset est un byte après l'opcode

**Syntaxe assembleur :**
```
STA <$50    ; Stocke A à l'adresse (DP << 8) | 0x50
LDA <$20    ; Charge A depuis l'adresse (DP << 8) | 0x20
```

**Exemples d'opcodes :**
- `0x97` : `STA <addr` - Stocke A en direct page
- `0xD7` : `STB <addr` - Stocke B en direct page
- `0x96` : `LDA <addr` - Charge A depuis direct page (pas encore implémenté)

**Format en mémoire :**
```
[Opcode] [Offset 8 bits]
  0x97      0x50
```

**Exemple avec DP = 0x00 :**
- Instruction : `STA <$50`
- DP = 0x00
- Offset = 0x50
- Adresse effective = (0x00 << 8) | 0x50 = 0x0050
- Stocke A à l'adresse 0x0050

**Exemple avec DP = 0x10 :**
- Instruction : `STA <$50`
- DP = 0x10
- Offset = 0x50
- Adresse effective = (0x10 << 8) | 0x50 = 0x1050
- Stocke A à l'adresse 0x1050

**Implémentation actuelle :**
```java
private void opSTAdirect() {
    // Mode DIRECT PAGE: adresse = (DP << 8) | offset
    int offset = fetchByte() & 0xFF;
    int addr = (regDP << 8) | offset;  // Utilise le registre DP !
    memory.writeByte(addr, (byte) regA);
    updateNZFlags(regA);
}
```

---

### 3. Mode ÉTENDU (addr)

**Caractéristiques :**
- ✅ Adresse complète 16 bits directement dans l'instruction
- ✅ **N'utilise PAS le registre DP**
- ✅ Permet d'adresser n'importe où dans les 64 Ko
- ✅ Plus lent (2 bytes d'adresse)

**Syntaxe assembleur :**
```
STA $1234    ; Stocke A à l'adresse absolue 0x1234
LDA $5678    ; Charge A depuis l'adresse absolue 0x5678
```

**Format en mémoire :**
```
[Opcode] [Adresse High] [Adresse Low]
  0xB7       0x12         0x34
```

**⚠️ Pas encore implémenté dans notre simulateur**

---

### 4. Mode INDEXÉ (régistre + offset)

**Caractéristiques :**
- ✅ Utilise un registre index (X, Y, U, ou S)
- ✅ Peut avoir un offset (+/-)
- ✅ Peut avoir auto-incrément/décrément
- ✅ **N'utilise PAS le registre DP** (sauf variantes spéciales)

**Syntaxe assembleur :**
```
LDA ,X       ; Charge A depuis l'adresse dans X
LDA 5,X      ; Charge A depuis X + 5
STA ,Y       ; Stocke A à l'adresse dans Y
```

**⚠️ Pas encore implémenté dans notre simulateur**

---

## 🔍 Résumé des Différences

| Mode | Utilise DP ? | Valeur/Adresse | Exemple |
|------|--------------|----------------|---------|
| **IMMÉDIAT** | ❌ NON | Dans l'instruction | `LDA #$42` |
| **DIRECT PAGE** | ✅ OUI | (DP << 8) \| offset | `STA <$50` |
| **ÉTENDU** | ❌ NON | Adresse 16 bits complète | `LDA $1234` |
| **INDEXÉ** | ❌ NON* | Registre + offset | `LDA ,X` |

*Certaines variantes indexées peuvent utiliser DP, mais c'est rare.

---

## ✅ Ce qui est Corrigé

1. **Noms des fonctions corrigés :**
   - ❌ `opSTAimmediate()` → ✅ `opSTAdirect()`
   - ❌ `opSTBimmediate()` → ✅ `opSTBdirect()`
   - etc.

2. **Commentaires clarifiés :**
   - Mode IMMÉDIAT : Ne touche pas au registre DP
   - Mode DIRECT PAGE : Utilise le registre DP

3. **Code plus clair :**
   - Les instructions en mode immédiat ne référencent plus DP
   - Les instructions en mode direct page expliquent clairement l'utilisation de DP

---

## 💡 Pourquoi cette Confusion ?

Les opcodes en mode DIRECT PAGE sont souvent appelés "direct" ou "zero page" dans d'autres architectures. Le 6809 utilise le terme "direct page" et utilise un registre DP pour permettre de pointer n'importe quelle "page" de 256 bytes dans les 64 Ko.

C'est différent du mode immédiat où la valeur est littéralement dans l'instruction, sans aucun calcul d'adresse ni accès mémoire.

