import { Component, OnInit } from '@angular/core';
import {TreeTableModule} from 'primeng/treetable';
import {TreeNode} from 'primeng/api';
import { SoftwareService } from '../generated/software/api/software.service';
import { NodeService } from './nodeservice';

@Component({
  selector: 'app-software',
  templateUrl: './software.component.html',
  styleUrls: ['./software.component.scss']
})
export class SoftwareComponent implements OnInit {

  software: TreeNode[];
    
  constructor(private nodeService: NodeService) { 
    
  }


  ngOnInit() {

    this.nodeService.getFilesystem().then(files => {console.log ("Software loaded : " + files); 
      this.software = files});
    

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


