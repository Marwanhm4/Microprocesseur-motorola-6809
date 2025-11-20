# Motorola 6809 Microprocessor Simulator

A pure Java implementation of a Motorola 6809 microprocessor simulator.

## Project Structure

```
MOTOROLA 6809/
├── src/
│   └── com/
│       └── motorola6809/
│           └── simulator/
│               └── Memory.java          # Memory management (RAM/ROM)
├── test/
│   └── com/
│       └── motorola6809/
│           └── simulator/
│               └── MemoryTest.java      # Memory tests
└── README.md
```

## Phase 1: Core Data Structures

### Memory Class
- 64KB address space (0x0000-0xFFFF)
- RAM and ROM as separate byte arrays
- Address bounds checking
- Big-endian byte order (Motorola 6809 standard)
- ROM write protection

## Compilation and Execution

### Compile the source code:
```bash
javac -d . src/com/motorola6809/simulator/*.java
```

### Compile the tests:
```bash
javac -d . test/com/motorola6809/simulator/*.java
```

### Run the tests:
```bash
java com.motorola6809.simulator.MemoryTest
```

## Features Implemented

- ✅ Memory read/write operations (byte and word)
- ✅ RAM and ROM separation
- ✅ Address bounds checking
- ✅ ROM write protection
- ✅ Big-endian byte order support
- ✅ Memory region configuration (setRAM/setROM)
- ✅ Data loading methods (loadRAM/loadROM)

## Future Phases

- Phase 2: CPU Registers
- Phase 3: ALU (Arithmetic Logic Unit)
- Phase 4: Instruction Decoder
- Phase 5: Instruction Execution
- Phase 6: Complete CPU Integration

