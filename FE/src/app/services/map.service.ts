import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import * as L from 'leaflet';
import 'leaflet-draw';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { PuntoPoligono } from '../dtos/request/PuntoPoligono';
import { DatiCirconferenza } from '../dtos/request/DatiCirconferenza';
import { GetAllEventiResponse } from '../dtos/response/GetAllEventiResponse';

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
  markersAftersDraw: GetAllEventiResponse[] = [];

  currentLatMarker: string = '';
  currentLngMarker: string = '';
  counterMarkersMarker: number = 0;
  mapMarker: any;

  hasPoligono: boolean = false;
  layer: any = undefined;
  visualizzaMarkers: boolean = false;

  private backendUrl: string = 'http://localhost:8080/api/v1/luogo/';

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
        marker: false
      },
      edit: {
        featureGroup: drawFeatures,
      },
    });
    mapDraw.addControl(drawControl);

    mapDraw.on('draw:created', (e: any) => {
      this.layer = e.layer;

      if (drawFeatures.getLayers().length) {
        mapDraw.removeLayer(e.layer);
        this.toastr.warning('Non possono esistere più poligoni!');

      } else {
        drawFeatures.addLayer(this.layer);

        this.layer.on('remove', (e: any) => {
          this.getAllMarkerCoordinates().subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              this.mapDraw = this.placeMarkers(this.mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });
        });

        if (this.layer._latlng === undefined) {

          this.markersInsidePolygon(this.layer._latlngs[0]).subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              this.placeMarkers(this.mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });

        } else if (this.layer._latlngs === undefined) {

          const centroLat: string = this.layer._latlng.lat.toString().trim();
          const centroLng: string = this.layer._latlng.lng.toString().trim();
          const raggio: string = this.layer._mRadius.toString().trim();

          this.markersInsideCircle(centroLat, centroLng, raggio).subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              this.placeMarkers(mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });
        }
      }
    });

    this.mapDraw = mapDraw;
    return mapDraw;
  }

  markersInsidePolygon(punti: any): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();
    const request: PuntoPoligono[] = [];
    punti.forEach((punto: PuntoPoligono) => {
      let lat: string = punto.lat.toString();
      let lng: string = punto.lng.toString();
      request.push({ lat, lng });
    })

    return this.http.post<GetAllEventiResponse[]>(this.backendUrl + 'coordinateDentroPoligono', request, { headers: header });
  }

  markersInsideCircle(centroLat: string, centroLng: string, raggio: string): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();
    const request: DatiCirconferenza = { centroLat, centroLng, raggio };

    return this.http.post<GetAllEventiResponse[]>(this.backendUrl + 'coordinateDentroCirconferenza', request, { headers: header });
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

  getAllMarkerCoordinates(): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiResponse[]>(this.backendUrl + 'getAllMarkerCoordinates', { headers: header });
  }

  placeMarkers(mapMarker: any, allMarkerCoordinates: GetAllEventiResponse[]): any {

    mapMarker.eachLayer((layer: any) => {
      if (layer instanceof L.Marker) {
        mapMarker.removeLayer(layer);
      }
    });

    allMarkerCoordinates.forEach((coordinate: GetAllEventiResponse) => {
      let marker = L.marker([+coordinate.lat, +coordinate.lng]);
      marker.bindPopup(L.popup().setContent(coordinate.titolo));
      marker.addTo(mapMarker);
    });

    this.mapMarker = mapMarker;
    return mapMarker;
  }

  markersAggiornati(): GetAllEventiResponse[] {
    return this.markersAftersDraw;
  }

  removeLayers(map: any): any {
    map.eachLayer((layer: any) => {
      if (layer instanceof L.Polygon || layer instanceof L.Circle) {
        map.removeLayer(layer);
      }
    });
    this.mapDraw = map;
    return map;
  }

  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : ''
    });
  }
}
