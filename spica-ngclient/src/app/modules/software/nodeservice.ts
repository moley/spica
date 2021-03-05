import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { TreeNode } from 'primeng/api';
import { SoftwareInfo } from '../generated/software';

@Injectable()
export class NodeService {

    constructor(private http: HttpClient) { }

    getTreeNodesFromModel (softwareInfos: Array<SoftwareInfo>) : Array<TreeNode> {
      let treenodes: Array<TreeNode> = []; 
      for (let software of softwareInfos) {
        var treeNode: TreeNode = { data: software, children:[]};
        treenodes.push(treeNode);
        if (software.children)
          this.fillChildren(software, treeNode)

      }

      return treenodes;

    }

    private fillChildren (softwareInfo: SoftwareInfo, parent: TreeNode) {
    
      for(let i=0; i<softwareInfo.children?.length; i++){
        var treeNode: TreeNode = { data: softwareInfo.children[i], children:[]};
        this.fillChildren(softwareInfo.children[i], treeNode);
        parent.children.push(treeNode);
      }
    }

  

    
}