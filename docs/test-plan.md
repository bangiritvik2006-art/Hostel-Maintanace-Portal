# Test Plan

| Test | Input/action | Expected result |
|---|---|---|
| Create request | Valid reporter, room, category, priority and description | HTTP 201; request stored |
| Required validation | Missing reporter or description | HTTP 400 |
| Read requests | GET `/api/requests` | Request list returned |
| Search | GET `/api/requests?search=610` | Matching room/request returned |
| Status update | PATCH request status to RESOLVED | Status updated and notification event attempted |
| Staff assignment | PATCH `/api/requests/{id}/assign/{staffId}` | Request assigned to staff |
| Delete request | DELETE `/api/requests/{id}` | HTTP 204 |
| Dashboard | GET `/api/dashboard/stats` | Counts returned |
| Notification health | GET `/api/notifications/health` | `status: UP` |

Run backend tests: `cd backend && mvn test`
Run notification tests: `cd notification-service && mvn test`
