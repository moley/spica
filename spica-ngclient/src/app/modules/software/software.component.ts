import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { SoftwareInfo } from '../generated/software/model/softwareInfo';
import { SoftwareService } from '../generated/software/api/software.service';


@Component({
  selector: 'app-software',
  templateUrl: './software.component.html',
  styleUrls: ['./software.component.scss']
})
export class SoftwareComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'description'];
  dataSource: SoftwareInfo[];

  constructor(private softwareService: SoftwareService) { }

  ngOnInit() {
    this.softwareService.getSoftware().subscribe(
      x => this.dataSource = x,
      err => console.error('Observer got an error: ' + err),
      () => console.error('Observer finished'),
    );
  }

}
