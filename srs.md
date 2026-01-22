# Software Requirements Specification (SRS)
## Functional Requirements

---

## FR-1: Vehicle Weight In–Out Recording

**Description:**  
The system shall record vehicle weight for ice purchasing transactions using trucks and Tuk Tuks.

**Requirements:**
- The system shall record **weight-in** for each truck or Tuk Tuk entering the weighing process.
- The system shall record **weight-out** for each truck or Tuk Tuk after ice loading.
- The system shall calculate the **net ice weight** based on weight-in and weight-out.
- The system shall support the following vehicle types:
  - Truck
  - Tuk Tuk

---

## FR-2: Customer Identification by License Plate

**Description:**  
The system shall identify customers based on vehicle license plates during ice purchasing.

**Requirements:**
- The system shall identify the customer using the **vehicle license plate number**.
- The system shall match the license plate against the **customer list**.
- The system shall automatically assign the customer name to the transaction when a match is found.
- If the license plate is not found, the system shall allow manual customer selection or registration.
