import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SoftwareInfo, SoftwareService } from '../generated/software';

interface IdAndName {
  name: string,
  id: string
}

interface Relation {
  type: string,
  target: string
}


@Component({
  selector: 'app-softwaredetails',
  templateUrl: './softwaredetails.component.html',
  styleUrls: ['./softwaredetails.component.scss']
})

export class SoftwareDetailsComponent implements OnInit {

  @Input()
  software: SoftwareInfo;

  types: IdAndName[];
  status: IdAndName[];
  groups: IdAndName[];
  roles: IdAndName[];
  relationTypes: IdAndName[];
  deployments: IdAndName[];
  relations: Relation[];

  selectedType: IdAndName;
  selectedStatus: IdAndName;
  selectedRole: IdAndName;
  allsoftware: SoftwareInfo[];
  technologies: String [];
  

  constructor(private softwareService: SoftwareService, private route: ActivatedRoute) {

    this.types = [{name: 'Web Application', id:"WEB_APP"}, 
                  {name: 'Standalone Application', id:"STANDALONE_APP"}]

    this.status = [{name: 'Konzeptphase', id:"CONCEPT"}, 
                  {name: 'Implementierung', id:"IMPLEMENTATION"}, 
                  {name: 'Produktiv', id:"IN_PRODUCTION"}]

    this.roles = [{name: 'Product Owner', id:"PRODUCTOWNER"}, 
                  {name: 'Softwareentwickler', id:"DEVELOPER"}, 
                  {name: 'Tester', id:"TESTER"}]

    this.relationTypes = [{name: 'wird benutzt von', id:"USED_IN"}, 
                  {name: 'benutzt', id:"USES"}]

    this.deployments = [{name: 'On Premise', id: 'ON_PREMISE'}, 
                        {name: 'Datacenter', id: 'DATACENTER'}]
    this.groups = [{name: 'Shop', id: 'SHOP'}, 
                   {name: 'InfoSystem', id: 'INFOSYSTEM'}]                    

    this.technologies = ['Gradle', 'Java', 'C#'];

    

    this.selectedType = this.types[0]
    this.selectedStatus = this.status[0];
    this.selectedRole = this.roles[0];
   }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.softwareService.getSoftwareById(params.id).subscribe(
        x => this.software = x,
        err => console.error('Observer got an error: ' + err),
        () => console.info('Observer finished'),
      );
    });
    
    this.software = {name: 'Component1', id: '12', description: 'Description1', teammembers: [
      {user: "user1@spica.org", role:"productowner"}, 
      {user: "user2@spica.org", role:"Developer"} 
    ], 
    technologies: ['Gradle', 'Java']}

    this.allsoftware = [{name: "Other1"}, {name: "Other2"}];
    this.relations = [{type: 'USES', target: '1'}]
  }

}
