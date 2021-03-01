import { Component, OnInit } from '@angular/core';
import { SoftwareInfo } from '../generated/software';

interface IdAndName {
  name: string,
  id: string
}

interface Connection {
  type: string,
  target: string
}


@Component({
  selector: 'app-softwaredetails',
  templateUrl: './softwaredetails.component.html',
  styleUrls: ['./softwaredetails.component.scss']
})

export class SoftwareDetailsComponent implements OnInit {

  software: SoftwareInfo;
  types: IdAndName[];
  status: IdAndName[];
  roles: IdAndName[];
  connectionTypes: IdAndName[];
  deployments: IdAndName[];
  connections: Connection[];

  selectedType: IdAndName;
  selectedStatus: IdAndName;
  selectedRole: IdAndName;
  allsoftware: SoftwareInfo[];
  technologies: String [];
  

  constructor() {
    this.types = [{name: 'Web Application', id:"WEB_APP"}, 
                  {name: 'Standalone Application', id:"STANDALONE_APP"}]

    this.status = [{name: 'Konzeptphase', id:"CONCEPT"}, 
                  {name: 'Implementierung', id:"IMPLEMENTATION"}, 
                  {name: 'Produktiv', id:"IN_PRODUCTION"}]

    this.roles = [{name: 'Product Owner', id:"PRODUCTOWNER"}, 
                  {name: 'Softwareentwickler', id:"DEVELOPER"}, 
                  {name: 'Tester', id:"TESTER"}]

    this.connectionTypes = [{name: 'wird benutzt von', id:"USED_IN"}, 
                  {name: 'benutzt', id:"USES"}]

    this.deployments = [{name: 'On Premise', id: 'ON_PREMISE'}, 
                        {name: 'Datacenter', id: 'DATACENTER'}]

    this.technologies = ['Gradle', 'Java', 'C#'];

    

    this.selectedType = this.types[0]
    this.selectedStatus = this.status[0];
    this.selectedRole = this.roles[0];
   }

  ngOnInit() {
    this.software = {name: 'Component1', id: '12', description: 'Description1', teammembers: [
      {user: "user1@spica.org", role:"productowner"}, 
      {user: "user2@spica.org", role:"Developer"} 
    ], 
    technologies: ['Gradle', 'Java']}

    this.allsoftware = [{name: "Other1"}, {name: "Other2"}];
    this.connections = [{type: 'USES', target: '1'}]
  }

}
