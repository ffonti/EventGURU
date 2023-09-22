import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import * as L from 'leaflet';
import 'leaflet-draw';
import { ToastrService } from 'ngx-toastr';

const iconUrl = 'assets/marker_icon.png';
const iconDefault = L.icon({
  iconUrl,
  iconSize: [40, 40],
  iconAnchor: [12, 41],
  shadowUrl: '',
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

L.Marker.prototype.options.icon = iconDefault;

@Injectable({
  providedIn: 'root'
})
export class MapService {
  currentLat: string = '';
  currentLng: string = '';
  markers: string[] = [];
  mapDraw: any;

  currentLatMarker: string = '';
  currentLngMarker: string = '';
  counterMarkersMarker: number = 0;
  mapMarker: any;

  hasPoligono: boolean = false;
  layer: any = undefined;
  visualizzaMarkers: boolean = false;

  constructor(private http: HttpClient, private toastr: ToastrService) {
    this.counterMarkersMarker = 0;
  }

  initMapDraw(mapDraw: any): any {
    mapDraw = L.map('mapDraw', {
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

    tiles.addTo(mapDraw);

    const drawFeatures = new L.FeatureGroup();
    mapDraw.addLayer(drawFeatures);

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
        marker: {
          icon: iconDefault
        },
      },
      edit: {
        featureGroup: drawFeatures,
      },
    });
    mapDraw.addControl(drawControl);

    mapDraw.on('draw:created', (e: any) => {
      this.layer = e.layer;

      if (e.layerType === 'marker') {
        if (this.markers.length) {
          this.toastr.warning('Inserire un solo marker alla volta');
          this.toastr.info('Puoi rimuovere un marker cliccandolo');
        } else {
          this.currentLat = e.layer._latlng.lat.toString();
          this.currentLng = e.layer._latlng.lng.toString();
          const marker = L.marker([+this.currentLat, +this.currentLng]);
          marker.addTo(mapDraw);
          marker.on('click', (e: any) => {
            this.markers = [];
            marker.remove();
          });
        }
      } else if (drawFeatures.getLayers().length) {
        mapDraw.removeLayer(e.layer);
        this.toastr.warning('Non possono esistere più poligoni!');
      } else {
        drawFeatures.addLayer(this.layer);
      }
    });

    this.mapDraw = mapDraw;
    return mapDraw;
  }

  initMapMarker(mapMarker: any): any {
    mapMarker = L.map('mapMarker', {
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
    tiles.addTo(mapMarker);

    const drawFeatures = new L.FeatureGroup();
    mapMarker.addLayer(drawFeatures);

    const drawControl = new L.Control.Draw({
      draw: {
        rectangle: false,
        circlemarker: false,
        polyline: false,
        circle: false,
        polygon: false,
        marker: {
          icon: iconDefault
        },
      }
    });
    mapMarker.addControl(drawControl);

    mapMarker.on('draw:created', (e: any) => {
      if (this.counterMarkersMarker) {
        this.toastr.warning('Inserire un solo marker alla volta');
        this.toastr.info('Puoi rimuovere un marker cliccandolo');
        return;

      } else {
        this.currentLatMarker = e.layer._latlng.lat.toString();
        this.currentLngMarker = e.layer._latlng.lng.toString();
        console.log(this.currentLatMarker, this.currentLngMarker);

        const marker = L.marker([+this.currentLatMarker, +this.currentLngMarker]);
        marker.addTo(mapMarker);

        this.counterMarkersMarker++;
        marker.on('click', (e: any) => {
          marker.remove();
          this.counterMarkersMarker--;
        });
      }
    });

    this.mapMarker = mapMarker;
    return mapMarker;
  }

  getCurrentLat(): string {
    return this.currentLat;
  }

  getCurrentLng(): string {
    return this.currentLng;
  }

  getCurrentLatMarker(): string {
    return this.currentLatMarker;
  }

  getCurrentLngMarker(): string {
    return this.currentLngMarker;
  }

  addMarker(mapMarker: any, lat: number, lng: number): any {
    const marker = L.marker([lat, lng]);
    marker.addTo(mapMarker);
    marker.on('click', (e: any) => {
      this.markers = [];
      marker.remove();
    });
    this.mapMarker = mapMarker;
    return mapMarker;
  }
}
