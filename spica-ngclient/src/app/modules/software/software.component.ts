import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Software } from '../generated/software/model/software';
import { SoftwareService } from '../generated/software/api/software.service';


@Component({
  selector: 'app-software',
  templateUrl: './software.component.html',
  styleUrls: ['./software.component.scss']
})
export class SoftwareComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'description'];
  dataSource: Software[];

  constructor(private softwareService: SoftwareService) { }

  ngOnInit() {
    this.softwareService.getSoftware().subscribe(
      x => this.dataSource = x,
      err => console.error('Observer got an error: ' + err),
      () => console.error('Observer finished'),
    );
  }

}
