import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './search-result.reducer';
import { ISearchResult } from 'app/shared/model/search-result.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISearchResultUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISearchResultUpdateState {
  isNew: boolean;
}

export class SearchResultUpdate extends React.Component<ISearchResultUpdateProps, ISearchResultUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { searchResultEntity } = this.props;
      const entity = {
        ...searchResultEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/search-result');
  };

  render() {
    const { searchResultEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterSampleReactApp.searchResult.home.createOrEditLabel">
              <Translate contentKey="jhipsterSampleReactApp.searchResult.home.createOrEditLabel">Create or edit a SearchResult</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : searchResultEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="search-result-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="search-result-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="sourceLabel" for="search-result-source">
                    <Translate contentKey="jhipsterSampleReactApp.searchResult.source">Source</Translate>
                  </Label>
                  <AvField id="search-result-source" type="text" name="source" />
                </AvGroup>
                <AvGroup>
                  <Label id="textLabel" for="search-result-text">
                    <Translate contentKey="jhipsterSampleReactApp.searchResult.text">Text</Translate>
                  </Label>
                  <AvField id="search-result-text" type="text" name="text" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="search-result-url">
                    <Translate contentKey="jhipsterSampleReactApp.searchResult.url">Url</Translate>
                  </Label>
                  <AvField id="search-result-url" type="text" name="url" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/search-result" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  searchResultEntity: storeState.searchResult.entity,
  loading: storeState.searchResult.loading,
  updating: storeState.searchResult.updating,
  updateSuccess: storeState.searchResult.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchResultUpdate);
