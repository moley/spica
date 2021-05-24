import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SoftwareMetricsInfo, SoftwareService} from '../generated/software';
import {DomSanitizer} from "@angular/platform-browser";


@Component({
  selector: 'app-softwaremetrics',
  templateUrl: './softwaremetrics.component.html',
  styleUrls: ['./softwaremetrics.component.scss']
})
export class SoftwaremetricsComponent implements OnInit {

  @Input()
  softwaremetrics: SoftwareMetricsInfo;

  basicOptions;

  constructor(private softwareService: SoftwareService, private router: Router, private route: ActivatedRoute, private sanitizer: DomSanitizer) {
    this.basicOptions = {
      legend: {
        labels: {
          fontColor: '#495057'
        }
      },
      scales: {
        xAxes: [{
          ticks: {
            fontColor: '#495057'
          }
        }],
        yAxes: [{
          ticks: {
            fontColor: '#495057'
          }
        }]
      }
    };
  }

  ngOnInit() {
    var softwaremetricsParamInfo = {};
    this.softwareService.getSoftwareMetrics(softwaremetricsParamInfo).subscribe(
      x => {
        this.softwaremetrics = x;
        console.log("TargetDate: " + this.softwaremetrics.history.labels)
      },
      err => console.error('Call to get softwareMetrics by ID recieved an error: ' + err),
      () => console.info('Call to get softwareMetrics by ID finished')
    );

  }

  stepToOverview() {
    console.log("stepToOverview called")
    this.router.navigate(['/app/software']);

  }



}
