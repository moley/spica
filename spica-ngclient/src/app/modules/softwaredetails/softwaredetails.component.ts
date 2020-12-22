import { Component, OnInit } from '@angular/core';
import { SoftwareInfo } from '../generated/software';


@Component({
  selector: 'app-softwaredetails',
  templateUrl: './softwaredetails.component.html',
  styleUrls: ['./softwaredetails.component.scss']
})
export class SoftwareDetailsComponent implements OnInit {

  software: SoftwareInfo;

  constructor() { }

  ngOnInit() {
    this.software = {name: 'Component1', id: '12', description: 'Description1'}
  }

}
