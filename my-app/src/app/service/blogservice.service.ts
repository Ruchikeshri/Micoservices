// import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';

import { map } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class BlogserviceService {

  private url='http://localhost:8084/api/v1/blogs';
  constructor(private http:HttpClient) { }
  getAllBlogs()
  {
  return this.http.get<any>(`${this.url}`)
  .pipe(catchError(error =>{return throwError(error.message)}))
  }

}
