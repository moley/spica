import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SoftwareInfo, SoftwareService } from '../generated/software';
import { SoftwareConstantsInfo } from '../generated/software/model/softwareConstantsInfo';

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

  constants: SoftwareConstantsInfo = {};

  relations: Relation[];

  allsoftware: SoftwareInfo[];

  newTechnology: string;
  

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
    console.log(this.software.type)
    this.softwareService.updateSoftware(this.software.id, this.software).subscribe(
      x => this.software = x,
        err => console.error('Call to update software ' + this.software.id + ' by ID got an error: ' + err),
        () => console.info('Call to update software by ID finished)'),
      );
  }

  stepToOverview () {
    this.router.navigate(['/app/software']);

  }

  addTechnology () {

    this.software.technologies = [...this.software.technologies, this.newTechnology];
    this.newTechnology = ''
    console.log("Added : " + this.software.technologies)

  

  }

}
