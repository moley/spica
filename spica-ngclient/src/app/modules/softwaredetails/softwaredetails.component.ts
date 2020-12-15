import { Component, OnInit } from '@angular/core';
//import { Software } from '../software/software.model';

@Component({
  selector: 'app-softwaredetails',
  templateUrl: './softwaredetails.component.html',
  styleUrls: ['./softwaredetails.component.scss']
})
export class SoftwareDetailsComponent implements OnInit {

  //software: Software;

  constructor() { }

  ngOnInit() {
    //this.software = new Software('Component1', 12, 'Description1');
  }

}
