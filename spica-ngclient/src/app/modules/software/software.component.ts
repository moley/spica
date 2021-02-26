import { Component, OnInit } from '@angular/core';
import {FlatTreeControl} from '@angular/cdk/tree';
import { SoftwareInfo } from '../generated/software/model/softwareInfo';
import { SoftwareService } from '../generated/software/api/software.service';



/** Flat node with expandable and level information */
interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  level: number;
}


interface Node<T> {
  value: T;
  children: Node<T>[];
}


@Component({
  selector: 'app-software',
  templateUrl: './software.component.html',
  styleUrls: ['./software.component.scss']
})
export class SoftwareComponent implements OnInit {

  treedata = [{
    name: 'Spica',
    id: '1', 
    children: [ 
      {name: 'Spica-Server'}, 
      {name: 'Spica-CLI'}

    ]
  }];
    
  constructor(private softwareService: SoftwareService) { 
  }

  ngOnInit() {

    /**this.softwareService.getSoftware().subscribe(
      x => this.dataSource.data = x,
      err => console.error('Observer got an error: ' + err),
      () => console.info('Observer finished'),
    );**/
  }

  click () {
    console.log ("Hello")
  }



}


