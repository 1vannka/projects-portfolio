# Train Route Simulator

## Objective
Reinforce the principles of **object-oriented programming** in C#: encapsulation, data hiding, composition, and polymorphism.  
Develop a model simulating the movement of trains along an automated route and verify its correctness using unit tests.

---

## Overview
The program simulates a magnetic-levitation train system.  
A train travels through a route composed of various segment types — each affecting its motion by accelerating, decelerating, or stopping it.  
At the end of the route, the simulator outputs the result: whether the journey was successful or failed, and the total travel time.

---

## Core Components
- **Train** — defines the train’s attributes (mass, speed, acceleration, force) and calculates the time to cover a given distance.  
- **RegularMagneticPart** — a standard track segment without external influence.  
- **PowerMagneticPart** — a segment that applies positive or negative force (acceleration/deceleration).  
- **Station** — simulates a stop with an arrival speed limit.  
- **Route** — combines all segments and manages the train’s traversal process.

---

## Test Scenarios

| № | Route | Expected Result |
|---|--------|-----------------|
| 1 | Power path → Regular path | Success |
| 2 | Power path (force exceeds limit) → Regular path | Failure |
| 3 | Power path → Regular path → Station → Regular path | Success |
| 4 | Power path (speed exceeds station limit) → Station | Failure |
| 5 | Power path (speed exceeds route limit) → Station | Failure |
| 6 | Combination of accelerations and decelerations | Success |
| 7 | Only regular path | Failure |
| 8 | Power path (Y) → Power path (-2Y) | Failure |

---

## Technologies
- **C#**  
- **xUnit** — unit testing  
- **Rider** — development environment

---

## Result
The simulator calculates the train’s movement along the defined route, determining:
- whether the route was completed successfully;  
- the total travel time.
