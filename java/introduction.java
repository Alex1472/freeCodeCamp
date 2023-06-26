// Java is a multiparadigmic (object-oriented first of all), crossplatform,
// statically typed programming language
// Write once - run anywhere!

// Developmentd began in 1991.
// JDK beta: 1995
// First version 1996
// OpenJDK: 2007 - open source code
// Oracle bought Sun
// 2017 release train model (release every half year). Earlier feature driven
// release as feature will be ready
// JAMES GOSLING - JAVA'S FATHER

// - Java - programming language and platform(language and environment, 
//   where programs executed)
// - JVM(Java Virtual Machine) - virtual machine, loads and run 
//   programs(standard libraries not included)
// - JRE(Java Runtime Environment) - runtime environment
//   JRE = JVM + standard libraries
//   You need only JRE to run a program
// - JDK(Java Development Kit) - developer tool
//   JDK = JRE + java compiler(from java to java bite code) + some other tools
//   Today JDK is destributed only, JRE was destributed separatly earlier.
//   Because of this, you can just run file with java and run it.
//   (We compile java into intermediate byte code and run it, we always have compiler now)

// There are several JVMs:
// - HotSpot - Oracle Java (Open JDK)
// - Azul Zing
// - IBM J9
// ...
// All JVM are supported standard, that means if you code works on one JVM 
// it will work on another

// There are several java compilers:
// - javac - Oracle Java (Open JDK)
// - ECJ (Eclipse Compiler for java)
// - IBM Jikes 


// Languages on java platform:
// - Groovy
// - Kotlin
// - Clojure
// - Scala
// - Ceylon
// You can compile thess languages into byte code and use java jvm


// PROGRAM LIFECYCLE
// Program on every language has several intermediate states that ended interpretation
// The difference is in set of this states and which states saved on disk.

// C - ahead of time compilation, clang compiler
// Source code hello.c                                 On disk
// Intermediate state 1: AST (abstract systax tree)    in clang compiler memory
// Intermediate state 2: LLVM                          in clang compiler memory
// Intermediate state 3: Intel x64 Binary(.exe)        On disk
// Intermediate state 4: CPU Microcode                 In a processor
// Interpretation(execution)                           In a processor

// Javascript - Just-in-time compilation, V8
// Source code hello.js                                On disk
// Intermediate state: AST                             Into virtual machine V8
//     |      Intermadiate state: HIR                  Into virtual machine V8
//     |      Intermediate state: LIR                  Into virtual machine V8
// Intermediate state: Intel x64 Binary                Into virtual machine V8
// Intermediate state: CPU Microcode                   In a processor
// Interpretation(execution)                           In a processor

// Java JIT/Interpretation, javac, HotSpot VM
// Source code Hello.java                              On disk
// Intermediate state: AST                             in javac compiler
// Java bytecode(platform independent)                 On disk
//   C1 IR              C2 IR                          Into HotSpot VM
// Intermediate state: Intel x64 Binary                Into HotSpot VM 
// Intermediate state: CPU Microcode                   In a processor
// Interpretation(execution)                           In a processor

//Note, as for java we don't save binary file on disk, we can't 
//generate it based on the current processor abilities



