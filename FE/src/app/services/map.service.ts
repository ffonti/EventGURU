import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import * as L from 'leaflet';
import 'leaflet-draw';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { PuntoPoligono } from '../dtos/request/PuntoPoligono';
import { DatiCirconferenza } from '../dtos/request/DatiCirconferenza';
import { GetAllEventiResponse } from '../dtos/response/GetAllEventiResponse';
import { globalBackendUrl } from 'environment';

//configurazione dell'immagine del marker
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

/**
 * service per la configurazione della mappa
 */
@Injectable({
  providedIn: 'root'
})
export class MapService {
  currentLat: number = 0;
  currentLng: number = 0;
  markers: string[] = [];
  mapDraw: any;
  markersAftersDraw: GetAllEventiResponse[] = [];

  currentLatMarker: number = 0;
  currentLngMarker: number = 0;
  counterMarkersMarker: number = 0;
  mapMarker: any;

  hasPoligono: boolean = false;
  layer: any = undefined;
  visualizzaMarkers: boolean = false;

  private backendUrl: string = globalBackendUrl + 'luogo/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient, private toastr: ToastrService) {
    this.counterMarkersMarker = 0;
  }

  /**
   * inizializzazione e configurazione della mappa per poter disegnare i poligoni
   * @param mapDraw mappa da configurare
   * @returns mappa modificata
   */
  initMapDraw(mapDraw: any): any {
    //centro della mappa quando viene visualizzata
    mapDraw = L.map('mapDraw', {
      center: [41.9027835, 12.4963655], //Coordinate di Roma
      zoom: 10,
    });

    //la libreria si affida a openstreetmap, una mappa open source
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

    //aggiungo le features per disegnare i poligoni
    const drawFeatures = new L.FeatureGroup();
    mapDraw.addLayer(drawFeatures);

    /*modifico le features per disegnare i poligoni e piazzare i marker
    nello specifico, può solo disegnare circonferenze e poligoni di n vertici*/
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

    //inizializzo la mappa "pulendo" tutti i layers
    mapDraw.eachLayer((layer: any) => {
      if (layer instanceof L.Marker) {
        mapDraw.removeLayer(layer);
      }
    });
    this.counterMarkersMarker = 0;

    //quando viene disegnata una figura, viene eseguito quanto dentro
    mapDraw.on('draw:created', (e: any) => {
      this.layer = e.layer;

      //può esistere un solo layer alla volta
      if (drawFeatures.getLayers().length) {
        mapDraw.removeLayer(e.layer);
        this.toastr.warning('Non possono esistere più poligoni!');

      } else {
        //se non ci sono layer, lo aggiungo
        drawFeatures.addLayer(this.layer);

        //quando viene rimosso il layer
        this.layer.on('remove', (e: any) => {
          //prendo tutte le coordinate dei markers
          this.getAllMarkerCoordinates().subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              //assegno alla mappa tutti i markers
              this.mapDraw = this.placeMarkers(this.mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });
        });

        //caso in cui si sta disegnando un poligono
        if (this.layer._latlng === undefined) {

          //prendo solo i markers all'interno del poligono
          this.markersInsidePolygon(this.layer._latlngs[0]).subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              //piazzo i marker nella mappa
              this.placeMarkers(this.mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });

          //nel caso in cui si sta disegnando una circonferenza
        } else if (this.layer._latlngs === undefined) {

          const centroLat: number = this.layer._latlng.lat;
          const centroLng: number = this.layer._latlng.lng;
          const raggio: number = this.layer._mRadius;

          //prendo solo i markers all'interno del poligono
          this.markersInsideCircle(centroLat, centroLng, raggio).subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              //piazzo i marker nella mappa
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

  /**
   * inizializzazione e configurazione della mappa dell'organizzatore per poter disegnare i poligoni
   * @param mapDraw mappa da configurare
   * @returns mappa modificata
   */
  initMapDrawOrganizzatore(mapDraw: any): any {
    //centro della mappa quando viene visualizzata
    mapDraw = L.map('mapDraw', {
      center: [41.9027835, 12.4963655], //Coordinate di Roma
      zoom: 10,
    });

    //la libreria si affida a openstreetmap, una mappa open source
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

    //aggiungo le features per disegnare i poligoni
    const drawFeatures = new L.FeatureGroup();
    mapDraw.addLayer(drawFeatures);

    /*modifico le features per disegnare i poligoni e piazzare i marker
    nello specifico, può solo disegnare circonferenze e poligoni di n vertici*/
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

    //inizializzo la mappa "pulendo" tutti i layers
    mapDraw.eachLayer((layer: any) => {
      if (layer instanceof L.Marker) {
        mapDraw.removeLayer(layer);
      }
    });
    this.counterMarkersMarker = 0;

    //quando viene disegnata una figura, viene eseguito quanto dentro
    mapDraw.on('draw:created', (e: any) => {
      this.layer = e.layer;

      //può esistere un solo layer alla volta
      if (drawFeatures.getLayers().length) {
        mapDraw.removeLayer(e.layer);
        this.toastr.warning('Non possono esistere più poligoni!');

      } else {
        //se non ci sono layer, lo aggiungo
        drawFeatures.addLayer(this.layer);

        //quando viene rimosso il layer
        this.layer.on('remove', (e: any) => {
          //prendo tutte le coordinate dei markers dell'organizzatore
          this.getAllMarkerCoordinatesByOrganizzatore().subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              //assegno alla mappa tutti i markers
              this.mapDraw = this.placeMarkers(this.mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });
        });

        //caso in cui si sta disegnando un poligono
        if (this.layer._latlng === undefined) {

          //prendo solo i markers all'interno del poligono
          this.markersInsidePolygonByOrganizzatore(this.layer._latlngs[0]).subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              //piazzo i marker nella mappa
              this.placeMarkers(this.mapDraw, res);
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
              this.toastr.error(err.error.message);
            }
          });

          //nel caso in cui si sta disegnando una circonferenza
        } else if (this.layer._latlngs === undefined) {

          const centroLat: number = this.layer._latlng.lat;
          const centroLng: number = this.layer._latlng.lng;
          const raggio: number = this.layer._mRadius;

          //prendo solo i markers all'interno del poligono
          this.markersInsideCircleByOrganizzatore(centroLat, centroLng, raggio).subscribe({
            next: (res: GetAllEventiResponse[]) => {
              this.markersAftersDraw = res;
              //piazzo i marker nella mappa
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

  /**
   * metodo che costruisce il DTO con i vertici del poligono e ritorna solo i punti dentro il poligono
   * @param punti array con i vertici del poligono
   * @returns coordinate dei marker dentro il poligono
   */
  markersInsidePolygon(punti: any): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();
    const request: PuntoPoligono[] = [];
    punti.forEach((punto: PuntoPoligono) => {
      let lat: number = punto.lat;
      let lng: number = punto.lng;
      request.push({ lat, lng });
    })

    return this.http.post<GetAllEventiResponse[]>(this.backendUrl + 'coordinateDentroPoligono', request, { headers: header });
  }

  /**
   * metodo che costruisce il DTO con i dati della circonferenza e ritorna solo i punti all'interno
   * @param centroLat latitudine del centro
   * @param centroLng longitudine del centro
   * @param raggio raggio della circonferenza
   * @returns coordinate dei marker dentro la circonferenza
   */
  markersInsideCircle(centroLat: number, centroLng: number, raggio: number): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();
    const request: DatiCirconferenza = { centroLat, centroLng, raggio };

    return this.http.post<GetAllEventiResponse[]>(this.backendUrl + 'coordinateDentroCirconferenza', request, { headers: header });
  }

  /**
   * inizializzazione e configurazione della mappa per piazzare il marker
   * @param mapMarker mappa da configurare
   * @returns mappa modificata
   */
  initMapMarker(mapMarker: any): any {
    //centro della mappa quando viene visualizzata
    mapMarker = L.map('mapMarker', {
      center: [41.9027835, 12.4963655], //Coordinate di Roma
      zoom: 10,
    });

    //la libreria si affida a openstreetmap, una mappa open source
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

    //aggiungo le features per disegnare i poligoni
    const drawFeatures = new L.FeatureGroup();
    mapMarker.addLayer(drawFeatures);

    /*modifico le features, nello specifico è possibile solo piazzare i marker*/
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

    //inizializzo la mappa "pulendo" tutti i layers
    mapMarker.eachLayer((layer: any) => {
      if (layer instanceof L.Marker) {
        mapMarker.removeLayer(layer);
      }
    });
    this.counterMarkersMarker = 0;

    //quando viene disegnata una figura, viene eseguito quanto dentro
    mapMarker.on('draw:created', (e: any) => {

      //può esistere un solo marker alla volta
      if (this.counterMarkersMarker) {
        this.toastr.warning('Inserire un solo marker alla volta');
        this.toastr.info('Puoi rimuovere un marker cliccandolo');
        return;

      } else {
        this.currentLatMarker = e.layer._latlng.lat;
        this.currentLngMarker = e.layer._latlng.lng;

        //se non ci sono markers, lo aggiungo
        const marker = L.marker([+this.currentLatMarker, +this.currentLngMarker]);
        marker.addTo(mapMarker);

        this.counterMarkersMarker++;
        //al click sul marker viene rimosso
        marker.on('click', (e: any) => {
          marker.remove();
          this.counterMarkersMarker--;
        });
      }
    });

    this.mapMarker = mapMarker;
    return mapMarker;
  }

  //ritorno la latitudine del marker selezionato
  getCurrentLat(): number {
    return this.currentLat;
  }

  //ritorno la longitudine del marker selezionato
  getCurrentLng(): number {
    return this.currentLng;
  }

  //ritorno la latitudine del marker corrente
  getCurrentLatMarker(): number {
    return this.currentLatMarker;
  }

  //ritorno la longitudine del marker corrente
  getCurrentLngMarker(): number {
    return this.currentLngMarker;
  }

  /**
   * aggiungo un marker ad una determinata mappa
   * @param mapMarker mappa a cui aggiungere il marker
   * @param lat latitudine del marker
   * @param lng longitudine del marker
   * @returns mappa col marker aggiunto
   */
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

  /**
   * metodo per prendere le coordinate di tutti i markers
   * @returns array di DTO con le coordinate e i dati degli eventi
   */
  getAllMarkerCoordinates(): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiResponse[]>(this.backendUrl + 'getAllMarkerCoordinates', { headers: header });
  }

  /**
   * metodo per piazzare nella mappa i DTO con diversi markers
   * @param mapMarker mappa in cui piazzare i markers
   * @param allMarkerCoordinates array di DTO con i markers da piazzare
   * @returns mappa aggiornata con i markers
   */
  placeMarkers(mapMarker: any, allMarkerCoordinates: GetAllEventiResponse[]): any {

    //rimuovo tutti i layers della mappa
    mapMarker.eachLayer((layer: any) => {
      if (layer instanceof L.Marker) {
        mapMarker.removeLayer(layer);
      }
    });

    //aggiungo i marker alla mappa e aggiungo un popup con il titolo
    allMarkerCoordinates.forEach((coordinate: GetAllEventiResponse) => {
      let marker = L.marker([+coordinate.lat, +coordinate.lng]);
      marker.bindPopup(L.popup().setContent(coordinate.titolo));
      marker.addTo(mapMarker);
    });

    this.mapMarker = mapMarker;
    return mapMarker;
  }

  //ritorno tutti i marker dopo aver disegnto il poligono
  markersAggiornati(): GetAllEventiResponse[] {
    return this.markersAftersDraw;
  }

  /**
   * rimuovo tutti i poligoni disegnati nella mappa
   * @param map mappa da cui rimuovere i poligoni
   * @returns mappa aggiornata
   */
  removeLayers(map: any): any {
    map.eachLayer((layer: any) => {
      if (layer instanceof L.Polygon || layer instanceof L.Circle) {
        map.removeLayer(layer);
      }
    });
    this.mapDraw = map;
    return map;
  }

  /**
   * prendo dal database tutti i marker degli eventi creati da un dato organizzatore
   * @returns array di DTO con i dati degli eventi
   */
  getAllMarkerCoordinatesByOrganizzatore(): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiResponse[]>(this.backendUrl + 'getAllMarkerCoordinates/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  /**
   * prendo tutti gli eventi dentro un poligono di un dato orgnizzatore
   * @param punti vertici del poligono
   * @returns array di DTO con i dati dei markers
   */
  markersInsidePolygonByOrganizzatore(punti: any): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();
    const request: PuntoPoligono[] = [];
    //popolo il DTO
    punti.forEach((punto: PuntoPoligono) => {
      let lat: number = punto.lat;
      let lng: number = punto.lng;
      request.push({ lat, lng });
    })

    return this.http.post<GetAllEventiResponse[]>(this.backendUrl + 'coordinateDentroPoligono/' + localStorage.getItem('id')?.toString().trim(), request, { headers: header });
  }

  /**
   * prendo tutti gli eventi dentro una circonferenza di un dato orgnizzatore
   * @param centroLat latitudine del centro
   * @param centroLng longitudine del centro
   * @param raggio raggio della circonferenza
   * @returns array di DTO con i dati degli eventi
   */
  markersInsideCircleByOrganizzatore(centroLat: number, centroLng: number, raggio: number): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();
    const request: DatiCirconferenza = { centroLat, centroLng, raggio };

    return this.http.post<GetAllEventiResponse[]>(this.backendUrl + 'coordinateDentroCirconferenza/' + localStorage.getItem('id')?.toString().trim(), request, { headers: header });
  }

  //creo l'header con il token da mandare al backend
  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : ''
    });
  }
}
