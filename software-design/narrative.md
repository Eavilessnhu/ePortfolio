# Artifact Narrative: Software Design & Engineering

## Artifact Description
The artifact selected for this category is the **Contact Service** architecture, initially developed in Java during intermediate coursework. The system functions as a domain service layer managing contact records, unique identifiers, personal contact attributes, and CRUD operations within an in-memory data store. 

## Justification of Inclusion
This artifact demonstrates proficiency in object-oriented design patterns, defensive programming, and decoupled software architecture. Prior to enhancement, the codebase exhibited tight coupling, mutable internal state, and brittle parameter handling that could fail silently when receiving invalid inputs.

Through iterative refactoring, the artifact was redesigned to adhere strictly to SOLID principles:
- **Encapsulation & Validation:** Implemented strict constructor and method-level boundary validation to guarantee that business invariants (e.g., character limits, non-null requirements, format verification) cannot be bypassed.
- **Defensive Immutability:** Protected entity state by removing leaky setters and returning immutable references where applicable.
- **Automated Verification:** Engineered an exhaustive suite of JUnit unit test fixtures covering boundary conditions, edge cases, and exception handling paths, driving code coverage close to 100%.

## Course Outcomes Met
- **Outcome 1 (Design & Architecture):** Demonstrated the ability to design and evaluate computing solutions using industry-standard design patterns, modular decomposition, and clean architectural boundaries.
- **Outcome 3 (Defensive Programming):** Applied defensive coding standards and thorough validation pipelines to prevent invalid state mutation and guarantee system robustness.

## Reflection & Evolution
Revisiting this artifact highlighted the difference between code that simply passes basic functional tests and software engineered for maintainability and extensibility. Enhancing this system underscored the importance of comprehensive test fixtures; having automated test suites enabled aggressive refactoring of the internal service logic without risking regression.
