import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SoftwareInfo, SoftwareService, TeamMemberInfo } from '../generated/software';
import { SoftwareConstantsInfo } from '../generated/software/model/softwareConstantsInfo';
import { IdAndDisplaynameInfo } from '../generated/software/model/idAndDisplaynameInfo';


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

  constants: SoftwareConstantsInfo = {};

  relations: Relation[];

  allsoftware: SoftwareInfo[];

  

  constructor(private softwareService: SoftwareService, private router:Router, private route: ActivatedRoute) {
   }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.softwareService.getSoftwareById(params.id).subscribe(
        x => this.software = x,
        err => console.error('Call to get software by ID got an error: ' + err),
        () => console.info('Call to get software by ID finished (' + this.software.type + ")"),
      );
    });
    

    this.softwareService.getConstants().subscribe(
      x => this.constants = x, 
      err => console.error('Call to get software constants got an error: ' + err),
      () => console.info('Call to get software constants finished'),
    )

    
    this.allsoftware = [{name: "Other1"}, {name: "Other2"}];
    this.relations = [{type: 'USES', target: '1'}]
  }

  save () {
    console.log("save called for " + this.software.id)
    this.softwareService.updateSoftware(this.software.id, this.software).subscribe(
      x => this.software = x,
        err => console.error('Call to update software ' + this.software.id + ' by ID got an error: ' + JSON.stringify(err)),
        () => console.info('Call to update software by ID finished)'),
      );
  }

  stepToOverview () {
    console.log("stepToOverview called")
    this.router.navigate(['/app/software']);

  }

  addTechnology () {
    console.log("addTechnology called")

    
    var newTechObject: IdAndDisplaynameInfo = {};

    this.software.technologies = [...this.software.technologies, newTechObject ];
    console.log("Added : " + this.software.technologies)
  }
  
  removeTechnology (technology: IdAndDisplaynameInfo) {
    console.log("removetechnology called with selected technology " + JSON.stringify(technology));
    this.software.technologies = this.software.technologies.filter(obj => obj !== technology)
  }

  removeRelation (relation: Relation) {
    console.log ("removeRelation called with selected relation " + JSON.stringify(relation))
    this.relations = this.relations.filter(obj => obj !== relation)
  }

  removeContact (contact: TeamMemberInfo) {
    console.log ("removeContact called with selected contact " + JSON.stringify(contact))
    this.software.teammembers = this.software.teammembers.filter(obj => obj !== contact)
  }

  addContact () {
    console.log ("addContact called")
    var newTeamMember: TeamMemberInfo = {}
    this.software.teammembers = [...this.software.teammembers, newTeamMember];
  }

  addRelation () {
    console.log ("addRelation called")
    var newRelation: Relation = {type: 'uses', target: null}
    this.relations = [...this.relations, newRelation];
  }

}
