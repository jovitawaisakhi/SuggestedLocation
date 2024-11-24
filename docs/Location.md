# Location API Spec

## Get Location
End point : GET /suggestion


### Query Parameters
- **name** (required): The search query to find suggestions (e.g., city name).
- **latitude** (optional): The latitude for a specific location.
- **longitude** (optional): The longitude for a specific location.

### Example Request :
GET /suggestion?name=Amh&latitude=4783345&longitude=-6919874


### Response Body :
```json
{
  "data": [
    {
      "name": "Amherst",
      "latitude": 45.83345,
      "longitude": -64.19874,
      "score": 0.053541430610647534
    },
    {
      "name": "Amherst Center",
      "latitude": 42.37537,
      "longitude": -72.51925,
      "score": 0.0
    },
    {
      "name": "North Amherst",
      "latitude": 42.41037,
      "longitude": -72.53092,
      "score": 0.0
    },
    {
      "name": "Amherstburg",
      "latitude": 42.11679,
      "longitude": -83.04985,
      "score": 0.0
    }
  ],
  "error": null
}
```