import { Component, OnInit } from '@angular/core';
import {TreeTableModule} from 'primeng/treetable';
import {TreeNode} from 'primeng/api';
import { SoftwareService } from '../generated/software/api/software.service';
import { NodeService } from './nodeservice';
import { SoftwareInfo } from '../generated/software';
import { Router } from '@angular/router';


@Component({
  selector: 'app-software',
  templateUrl: './software.component.html',
  styleUrls: ['./software.component.scss']
})
export class SoftwareComponent implements OnInit {

  software: Array<TreeNode>;

  constructor(private softwareService: SoftwareService, private nodeService: NodeService, private router: Router) { 
    
  }


  ngOnInit() {
    this.softwareService.getSoftware().subscribe(
      x => this.software = this.nodeService.getTreeNodesFromModel(x),
      err => console.error('Observer got an error: ' + err),
      () => console.info('Observer finished'),
    );
  }

  stepToDetails (software: SoftwareInfo) {
    console.log ("Hello " + software.id)
    this.router.navigate(['app/software/', software.id]);

  }






}


