import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import * as L from 'leaflet';
import { icon, Marker } from 'leaflet';
import 'leaflet-draw';
import { ToastrService } from 'ngx-toastr';

const iconUrl = 'assets/marker_icon.png';
const iconDefault = icon({
  iconUrl,
  iconSize: [40, 40],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
});
Marker.prototype.options.icon = iconDefault;

@Injectable({
  providedIn: 'root'
})
export class MapService {
  currentLat: string = '';
  currentLng: string = '';
  hasPoligono: boolean = false;
  layer: any = undefined;
  map: any;
  visualizzaMarkers: boolean = false;

  constructor(private http: HttpClient, private toastr: ToastrService) { }

  initMap(map: any): any {
    map = L.map('map', {
      center: [41.9027835, 12.4963655], //Coordinate di Roma
      zoom: 10,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }
    );

    tiles.addTo(map);

    const drawFeatures = new L.FeatureGroup();
    map.addLayer(drawFeatures);

    const drawControl = new L.Control.Draw({
      draw: {
        rectangle: false,
        circlemarker: false,
        polyline: false,
        polygon: {
          allowIntersection: false,
          shapeOptions: {
            color: '#145DA0',
            fillOpacity: 0.1,
          },
        },
        circle: {
          shapeOptions: {
            color: '#145DA0',
            fillOpacity: 0.1,
          },
        },
      },
      edit: {
        featureGroup: drawFeatures,
      },
    });
    map.addControl(drawControl);

    map.on('draw:created', (e: any) => {
      this.layer = e.layer;
      if (e.layerType === 'marker') {
        this.currentLat = e.layer._latlng.lat.toString();
        this.currentLng = e.layer._latlng.lng.toString();
        const marker = L.marker([+this.currentLat, +this.currentLng]);
        marker.addTo(map).bindPopup('ciao');
      } else if (drawFeatures.getLayers().length) {
        map.removeLayer(e.layer);
        this.toastr.warning('Non possono esistere più poligoni!');
      } else {
        drawFeatures.addLayer(this.layer);
      }
    });

    this.map = map;
    return map;
  }

  getCurrentLat(): string {
    return this.currentLat;
  }

  getCurrentLng(): string {
    return this.currentLng;
  }
}
