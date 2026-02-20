# Create Coupon API

## Endpoint
**POST** `/api/v1/coupons`

---

## Description
This API creates a Coupon, representing a weighing session of a vehicle at the truck weighing station.

A coupon includes:

The vehicle being weighed

The employee performing the weighing

Multiple weighing records that reflect accumulated weight over time

Product pricing and quantity configuration with customer-specific overrides and default fallback

---

## Request Body Example

```json
{
  "vehicleId": "333feb09-bea9-444a-9dda-4c326e0cf914",
  "driverName": "Nguyen Phu Hoi",
  "employeeId": "92b1b2f4-029b-40dd-aba5-5891ecaa1664",
  "remark": "buy 10kg solid ice",
  "weightRecords": [
    {
      "productName": null,
      "unit": null,
      "pricePerProduct": null,
      "quantityPerProduct": null,
      "quantity": null,
      "weight": null,
      "outTime": "2026-02-10T08:16:58.011Z",
      "memo": null,
      "manual": true
    },
    {
      "productName": "solid ice",
      "unit": "can",
      "pricePerProduct": 10,
      "quantityPerProduct": null,
      "quantity": 100,
      "weight": 1008,
      "outTime": "2026-02-10T08:16:58.011Z",
      "memo": null,
      "manual": true
    },
    {
      "productName": "ice cubes",
      "unit": "can",
      "pricePerProduct": 200,
      "quantityPerProduct": 10,
      "quantity": 100,
      "weight": 1218,
      "outTime": "2026-02-10T08:16:58.011Z",
      "memo": "daniel test memo",
      "manual": true
    }
  ],
  "couponNo": 19,
  "couponId": 19,
  "accNo": "string",
  "delAccNo": "string",
  "delDate": "2026-02-10T07:42:55.196Z"
}
```
## Field Description

### Top-level Fields

| Field | Type | Required | Description |
|------|------|----------|-------------|
| `vehicleId` | UUID | Yes | Vehicle currently entering the truck weighing station. The vehicle must belong to the selected customer. |
| `driverName` | String | No | Name of the vehicle driver. |
| `employeeId` | UUID | Yes | Staff member performing the weighing process. |
| `remark` | String | No | Additional notes or comments for the coupon. |
| `weightRecords` | Array | Yes | List of weighing records that represent the full weighing history of the vehicle. |
| `couponNo` | Number | No | Coupon number used for business or operational reference. |
| `couponId` | Number | No | Coupon identifier for legacy or external system mapping. |
| `accNo` | String | No | Accounting reference number. |
| `delAccNo` | String | No | Delivery accounting number. |
| `delDate` | ISO DateTime | No | Delivery date and time. |

---

### Weight Record Fields

| Field | Type | Required | Description |
|------|------|----------|-------------|
| `productName` | String \| null | No | Name of the weighed product. `null` indicates raw vehicle weighing (no product). |
| `unit` | String \| null | No | Unit of the weighed product (e.g. `can`, `kg`). |
| `pricePerProduct` | Number \| null | No | Current price per product unit at the time of weighing. |
| `quantityPerProduct` | Number \| null | No | Quantity that represents **1 unit** for this customer. |
| `quantity` | Number \| null | No | Quantity of product added during this weighing step. |
| `weight` | Number \| null | No | Total measured weight at this weighing step. |
| `outTime` | ISO DateTime | No | Timestamp when the weighing was performed. |
| `memo` | String \| null | No | Optional memo or note for this weighing record. |
| `manual` | Boolean | No | Indicates whether the weighing record was entered manually. |

---

# Weighing Process Rules

The `weightRecords` array **must follow an accumulated weighing sequence** as described below:

---

## 1. First Record — Raw Vehicle Weighing
- Represents the vehicle weight **without any products**
- `productName`: `null`

---

## 2. Second Record — Raw Vehicle + First Product
- Represents:
    - Vehicle weight
    - Weight of the **first product**
- Accumulated weight = `vehicle + product_1`

---

## 3. Third Record — Raw Vehicle + First Product + Second Product
- Represents:
    - Vehicle weight
    - Weight of the **first product**
    - Weight of the **second product**
- Accumulated weight = `vehicle + product_1 + product_2`

---

## 4. Subsequent Records — Continue Accumulation
- Each new record:
    - Adds **one additional product**
    - Includes all previously added products
- Accumulated weight grows progressively with each record

---

## Summary
- Weighing is **cumulative**
- Each record builds on the previous one
- The first record always represents the **raw vehicle**
- Products are added **one by one** in order

---
# Data Sources & Related APIs

## Vehicle Selection

Retrieve available vehicles for a customer:

```GET /api/v1/customers/{customerId}/vehicles```

---

## Employee Selection

Retrieve weighing staff:
```GET /api/v1/employees```


---

## Product Information

Retrieve product name and unit:

```aiignore
GET /api/v1/products
GET /api/v1/products/{productId}
```


---

## Product Pricing & Quantity Configuration

Pricing and quantity configuration follow this **priority order**:

1. **Customer-specific product settings**
2. **Default product settings** (fallback)

---

### Customer Product Settings

Retrieve customer-specific product configurations:
```GET /api/v1/customers/{customerId}/product-settings```

---

### Default Product Settings

Retrieve default product configurations:
```aiignore
GET /api/v1/products
GET /api/v1/products/{productId}
```


---

## Field Resolution Rules

| Field               | Resolution Rule                                      |
|---------------------|------------------------------------------------------|
| `pricePerProduct`   | Customer setting → fallback to product default       |
| `quantityPerProduct`| Customer setting → fallback to product default       |