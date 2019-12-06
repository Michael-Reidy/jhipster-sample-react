import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SearchResult from './search-result';
import SearchResultDetail from './search-result-detail';
import SearchResultUpdate from './search-result-update';
import SearchResultDeleteDialog from './search-result-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SearchResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SearchResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SearchResultDetail} />
      <ErrorBoundaryRoute path={match.url} component={SearchResult} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SearchResultDeleteDialog} />
  </>
);

export default Routes;
