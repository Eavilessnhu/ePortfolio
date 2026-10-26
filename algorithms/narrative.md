# Artifact Narrative: Algorithms & Data Structures

## Artifact Description
The artifact selected for this category focuses on **Cryptographic Checksum Verification and Algorithmic Lookup Optimization**, developed within a secure computing context. The system provides data verification pipelines, digest generations, and integrity auditing across files and transactional streams.

## Justification of Inclusion
This artifact illustrates computational problem solving, asymptotic performance analysis, and the implementation of robust hashing algorithms. Prior to enhancement, verification logic relied on naive linear traversals that scaled poorly under heavier workloads and lacked formal protection against data corruption or intentional tampering.

The refactoring targeted both efficiency and security:
* **Algorithmic Complexity Reduction:** Replaced brute-force $O(n)$ search sequences with hash-based structures to achieve near $O(1)$ constant-time lookups for stored signatures.
* **Cryptographic Rigor:** Integrated the Java Cryptography Architecture (JCA) using SHA-256 digests, ensuring data integrity verification adheres to industry standards.
* **Stream Buffering:** Optimized buffer chunk sizes during I/O reads to avoid high heap memory spikes when hashing large payloads.

## Course Outcomes Met
* **Outcome 2 (Computational Thinking & Optimization):** Evaluated and applied data structures and algorithmic approaches, analyzing trade-offs in time and space complexity ($O$-notation).
* **Outcome 4 (Security Mindset):** Implemented defense-in-depth principles and cryptographic primitives to protect systems and data flows from tampering.

## Reflection & Evolution
Refactoring this artifact emphasized that algorithmic correctness is only half the battle—efficiency and resource constraints dictate real-world success. Moving from theoretical big-O notation to profiling actual throughput and memory consumption solidified my understanding of balancing computational overhead
