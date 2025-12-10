# Processus de Développement du Simulateur 6809

## 📊 État Actuel du Projet

### ✅ Ce qui est FAIT

#### Phase 1 : Mémoire (100% complète)
- ✅ RAM et ROM séparées
- ✅ Espace d'adressage 64 Ko (0x0000-0xFFFF)
- ✅ Vérification des limites d'adresse
- ✅ Big-endian (standard Motorola)
- ✅ Protection ROM en écriture

#### Phase 2 : CPU - Structure de Base (100% complète)
- ✅ Tous les registres (A, B, X, Y, U, S, PC, DP, CC)
- ✅ Condition codes (flags: C, V, Z, N, I, H, F, E)
- ✅ Méthode `step()` pour exécuter une instruction
- ✅ Méthodes `fetchByte()` et `fetchWord()`

#### Phase 3 : Instructions - Modes d'Adressage (Partiellement complète)

**✅ Mode Immédiat (#imm)** - COMPLET
- LDA, LDB, LDX, LDY, LDU avec valeur immédiate
- ADDA, ADDB, SUBA, SUBB avec valeur immédiate
- ANDA, ANDB, ORA, ORB, EORA, EORB avec valeur immédiate

**✅ Mode Inhérent** - COMPLET
- INCA, DECA, CLRA, COMA, NEGA, TSTA
- INCB, DECB, CLRB, COMB, NEGB, TSTB
- ROLA, ROLB, RORA, RORB
- ASLA, ASLB, ASRA, ASRB, LSRA, LSRB
- INX, DEX, INY, DEY, ABX
- PSHA, PSHB, PULA, PULB, PSHS, PULS
- MUL, SEX, NOP, SYNC

**✅ Mode Direct Page** - PARTIEL
- STA, STB, STX, STY (stockage avec adresse directe)
- Note: Les instructions de chargement depuis mémoire directe ne sont pas encore implémentées

**✅ Branches Relatives Courtes** - COMPLET
- BRA, BNE, BEQ, BCC, BCS, BPL, BMI, BGT, BLE, BHI, BLS

### ❌ Ce qui MANQUE (Prochaine Étape)

#### Mode d'Adressage Indexé (PRIORITÉ #1)
Le 6809 est célèbre pour ses modes d'adressage indexés puissants. Actuellement, on ne peut pas faire :
- `LDA ,X` - Charger A depuis l'adresse dans X
- `LDA 5,X` - Charger A depuis X + 5
- `STA ,Y` - Stocker A à l'adresse dans Y
- `LDA -10,X` - Charger A depuis X - 10

**Modes indexés à implémenter :**
1. **Indexé sans offset** : `LDA ,X` (adresse = contenu de X)
2. **Indexé avec offset 5 bits** : `LDA 5,X` (offset signé -16 à +15)
3. **Indexé avec offset 8 bits** : `LDA 100,X` (offset signé -128 à +127)
4. **Indexé avec offset 16 bits** : `LDA 1000,X` (offset 0 à 65535)
5. **Indexé avec auto-incrément/décrément** : `LDA ,X+` ou `LDA ,-X`
6. **Indexé avec accumulation** : `LDA A,X` (adresse = X + A)

#### Mode d'Adressage Étendu
- `LDA $1234` - Charger depuis adresse absolue 16 bits
- `STA $5678` - Stocker à adresse absolue 16 bits

#### Mode d'Adressage Relatif Long
- `LBRN`, `LBRA`, `LBNE`, etc. - Branches avec offset 16 bits

#### Instructions Manquantes avec Modes Existants
- `LDA <addr` - Charger depuis direct page (mode direct)
- `STA <addr` - Stocker vers direct page
- `JSR <addr` - Saut vers sous-programme
- `BSR` - Branch to subroutine

---

## 🔄 Processus de Développement d'un Simulateur

### Étape 1 : Comprendre l'Architecture
```
1. Lire la documentation du processeur (datasheet 6809)
2. Comprendre les registres et leur rôle
3. Comprendre les modes d'adressage
4. Comprendre le format des instructions
```

### Étape 2 : Implémenter la Mémoire
```
1. Créer la structure de données pour la mémoire
2. Implémenter readByte/writeByte
3. Ajouter la vérification des limites
4. Tester avec des opérations simples
```

### Étape 3 : Implémenter le CPU de Base
```
1. Créer les registres
2. Implémenter fetch (lecture d'opcodes)
3. Créer la structure step() avec switch
4. Implémenter reset()
```

### Étape 4 : Implémenter les Instructions (Mode par Mode)
```
Pour chaque mode d'adressage :
1. Comprendre comment calculer l'adresse effective
2. Créer une méthode helper pour ce mode
3. Implémenter les instructions qui utilisent ce mode
4. Tester chaque instruction
```

### Étape 5 : Refactoriser et Optimiser
```
1. Regrouper le code commun
2. Créer des helpers réutilisables
3. Optimiser les performances
4. Ajouter des tests complets
```

---

## 🎯 Prochaine Étape Recommandée : Mode Indexé

### Pourquoi le Mode Indexé en Premier ?

1. **Très utilisé** : Le 6809 utilise massivement l'adressage indexé
2. **Puissant** : Permet d'accéder aux tableaux et structures de données
3. **Fondamental** : Beaucoup d'instructions nécessitent ce mode
4. **Logique** : Complète les modes déjà implémentés

### Comment Implémenter le Mode Indexé ?

#### Étape 1 : Créer une Méthode Helper pour Calculer l'Adresse Effective

```java
/**
 * Calcule l'adresse effective pour le mode indexé
 * @param postByte Le byte post-instruction qui encode le mode
 * @return L'adresse effective calculée
 */
private int calculateIndexedAddress(int postByte) {
    int register = (postByte >> 5) & 0x03; // Bits 5-6: registre (X=00, Y=01, U=10, S=11)
    int mode = (postByte >> 2) & 0x07;      // Bits 2-4: mode d'adressage
    int indirect = (postByte >> 1) & 0x01;   // Bit 1: indirect
    int autoModify = postByte & 0x01;       // Bit 0: auto-modification
    
    int baseAddress = 0;
    switch (register) {
        case 0: baseAddress = regX; break;  // X
        case 1: baseAddress = regY; break;  // Y
        case 2: baseAddress = regU; break;  // U
        case 3: baseAddress = regS; break;   // S
    }
    
    int offset = 0;
    switch (mode) {
        case 0: // Pas d'offset
            offset = 0;
            break;
        case 1: // Offset 5 bits signé (-16 à +15)
            int offset5 = fetchByte() & 0x1F;
            offset = (offset5 & 0x10) != 0 ? offset5 | 0xFFFFFFE0 : offset5;
            break;
        case 2: // Offset 8 bits signé (-128 à +127)
            offset = fetchSignedByte();
            break;
        case 3: // Offset 16 bits
            offset = fetchSignedWord();
            break;
        case 4: // Auto-incrément
            baseAddress = (baseAddress + 1) & 0xFFFF;
            // Mettre à jour le registre
            break;
        // ... autres modes
    }
    
    return (baseAddress + offset) & 0xFFFF;
}
```

#### Étape 2 : Implémenter les Instructions avec Mode Indexé

```java
// Exemple : LDA avec mode indexé
case 0xA6: // LDA avec mode indexé
    int addr = calculateIndexedAddress(fetchByte());
    regA = memory.readByte(addr) & 0xFF;
    updateNZFlags(regA);
    break;
```

#### Étape 3 : Tester

```java
// Test : LDA ,X (charger depuis l'adresse dans X)
cpu.setX(0x1000);
memory.writeByte(0x1000, (byte)0x42);
// Écrire instruction LDA ,X à l'adresse 0x0010
memory.writeByte(0x0010, (byte)0xA6); // LDA indexé
memory.writeByte(0x0011, (byte)0x84); // Mode: X, pas d'offset
cpu.step();
assertEquals(0x42, cpu.getA());
```

---

## 📋 Plan d'Implémentation Recommandé

### Phase Actuelle : Mode Indexé (Priorité 1)

1. **Créer la méthode `calculateIndexedAddress()`**
   - Gérer tous les sous-modes indexés
   - Gérer les auto-incréments/décréments
   - Gérer les offsets

2. **Implémenter les instructions LOAD avec indexé**
   - `LDA` (0xA6)
   - `LDB` (0xE6)
   - `LDX` (0xAE)
   - `LDY` (0x10AE)
   - etc.

3. **Implémenter les instructions STORE avec indexé**
   - `STA` (0xA7)
   - `STB` (0xE7)
   - etc.

4. **Implémenter les instructions arithmétiques avec indexé**
   - `ADDA` (0xAB)
   - `SUBA` (0xA0)
   - etc.

5. **Tester chaque mode indexé**

### Phase Suivante : Mode Étendu (Priorité 2)

1. Implémenter les instructions avec adresse 16 bits
2. Tester

### Phase Suivante : Autres Modes (Priorité 3)

1. Mode relatif long
2. Mode indirect
3. Instructions manquantes

---

## 💡 Conseils pour le Développement

1. **Tester au fur et à mesure** : Ne pas attendre la fin pour tester
2. **Documenter** : Commenter chaque mode d'adressage
3. **Réutiliser** : Créer des helpers pour éviter la duplication
4. **Référence** : Garder la documentation 6809 à portée de main
5. **Itérer** : Commencer simple, puis ajouter la complexité

---

## 🔍 Ressources Utiles

- **Datasheet Motorola 6809** : Contient tous les opcodes et modes
- **Table de décodage** : Pour comprendre les post-bytes indexés
- **Exemples de code 6809** : Pour comprendre l'usage réel

