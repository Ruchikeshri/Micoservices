import { Component, OnInit } from '@angular/core';
import { BlogserviceService } from 'src/app/service/blogservice.service';
import {Blog} from 'src/app/model/Blog';
import {MatCardModule} from '@angular/material/card';
@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css']
})
export class BlogComponent implements OnInit {


  constructor(private blogservice:BlogserviceService) {
this.fav=new Blog();
  }
  fav:any
  ngOnInit(): void {
this.blogservice.getAllBlogs().subscribe(resp =>{
 console.log(resp);
this.fav=resp;
})
  }

}
