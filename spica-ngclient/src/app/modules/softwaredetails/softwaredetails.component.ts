import { Component, OnInit } from '@angular/core';
import { SoftwareInfo } from '../generated/software';

interface Type {
  name: string,
  id: string
}


@Component({
  selector: 'app-softwaredetails',
  templateUrl: './softwaredetails.component.html',
  styleUrls: ['./softwaredetails.component.scss']
})

export class SoftwareDetailsComponent implements OnInit {

  software: SoftwareInfo;
  types: Type[];
  selectedType: Type;

  constructor() {
    this.types = [{name: 'Web Application', id:"WEB_APP"}, 
                  {name: 'Standalone Application', id:"STANDALONE_APP"}]
    this.selectedType = this.types[0]
   }

  ngOnInit() {
    this.software = {name: 'Component1', id: '12', description: 'Description1'}
  }

}
